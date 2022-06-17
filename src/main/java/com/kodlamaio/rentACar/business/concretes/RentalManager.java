package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureServiceService;
import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.DeleteRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.UpdateRentalRequest;
import com.kodlamaio.rentACar.business.responses.rentals.GetAllRentalsResponse;
import com.kodlamaio.rentACar.business.responses.rentals.GetRentalResponse;
import com.kodlamaio.rentACar.core.adapters.FindeksValidationService;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.ErrorResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureServiceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.MaintenanceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.UserRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Rental;
import com.kodlamaio.rentACar.entities.concretes.User;

@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	
	@Autowired
	private AdditionalFeatureItemRepository itemRepository;
	
	@Autowired
	private AdditionalFeatureServiceRepository additionalFeatureServiceRepository;

	@Autowired
	private AdditionalFeatureServiceService additionalFeatureServiceService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FindeksValidationService findeksValidationService;
	
	@Autowired
	private CarService carService;

	@Autowired
	public RentalManager(RentalRepository rentalRepository, CarRepository carRepository,
			ModelMapperService modelMapperService) {
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
		this.modelMapperService = modelMapperService;

	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());
		checkIfCarState(createRentalRequest.getCarId());
		
		Car car = this.carRepository.findById(createRentalRequest.getCarId());
		User user = this.userRepository.findById(createRentalRequest.getUserId());
		checkIndividualCustomerFindexScoreAndCarFindexScore(car.getId(),user.getId());
		
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		
		car.setState(3);
		//rental.setPickUpCityId(car.getCity());
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		//rental.setPickUpCityId(car.getCity());
		car.setCity(rental.getReturnCityId());
		this.rentalRepository.save(rental);
		return new SuccessResult("CAR.RENTED");
	}

	@Override
	public DataResult<List<GetAllRentalsResponse>> getAll() {
		List<Rental> rentals = this.rentalRepository.findAll();
		List<GetAllRentalsResponse> response = rentals.stream()
				.map(rental -> this.modelMapperService.forResponse().map(rental, GetAllRentalsResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllRentalsResponse>>(response, "RENTED.CARS.LISTED");
	}
	
	

	@Override
	public DataResult<GetRentalResponse> getById(int id) {
		Rental rental = this.rentalRepository.findById(id);
		GetRentalResponse response = this.modelMapperService.forResponse().map(rental, GetRentalResponse.class);

		return new SuccessDataResult<GetRentalResponse>(response);
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		checkIfIdExists(updateRentalRequest.getId());

		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId());
		Rental rentalMapped = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		Car car = this.carRepository.findById(updateRentalRequest.getCarId());
		
		rental.setReturnDate(updateRentalRequest.getReturnDate());
		
		rental.setReturnCityId(rentalMapped.getReturnCityId());
		car.setCity(rentalMapped.getReturnCityId());
		
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		
		this.rentalRepository.save(rental);
		return new SuccessResult("RENTED.UPDATED");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		this.rentalRepository.deleteById(deleteRentalRequest.getId());
		return new SuccessResult("RENTED.DELETED");
	}

	private void checkIfDatesAreCorrect(LocalDate pickUpDate, LocalDate returnDate) {
		if (!pickUpDate.isBefore(returnDate) || pickUpDate.isBefore(LocalDate.now())) {
			throw new BusinessException("DATE.ERROR");
		}
	}

	private void checkIfCarState(int carId) {
		Car car = this.carRepository.findById(carId);
		if (car.getState() != 1) {
			throw new BusinessException("CAR.IS.NOT.AVAILABLE");
		}
	}

	private double calculateTotalPriceOfRental(Rental rental, double carDailyPrice) {
		double totalPrice = 0;
		int daysDifference = (int) ChronoUnit.DAYS.between(rental.getPickUpDate(), rental.getReturnDate());
		totalPrice = (carDailyPrice * daysDifference) +  
				+ calculatePriceByReturnLocation(rental.getPickUpCityId().getId(), rental.getReturnCityId().getId());
		rental.setTotalDays(daysDifference);
		return totalPrice;
	}

	private double calculatePriceByReturnLocation(int pickUpLocationId, int returnLocationId) {
		double additionalPrice = 0;
		if (pickUpLocationId != returnLocationId) {
			additionalPrice = 750;
		}
		return additionalPrice;
	}
	
	private void checkIfIdExists(int id) {
		Rental rental = this.rentalRepository.findById(id);
		if(rental == null) {
			throw new BusinessException("THERE.IS.NO.RENTED.CAR");
		}
	}
	
	private void checkIndividualCustomerFindexScoreAndCarFindexScore(int carId, int userId) {
		
		Car car = this.carRepository.findById(carId);
		User user = this.userRepository.findById(userId);
		int userFindexScore = this.findeksValidationService.calculateFindeksScoreOfUser(user.getIdentityNumber());
		System.out.println("Car score "+car.getMinFindeksScore());
		if(userFindexScore < car.getMinFindeksScore()) {
			throw new BusinessException("FINDEKS.SCORE.IS.NOT.ENOUGH");
		}
		
		
	}

	@Override
	public DataResult<List<GetAllRentalsResponse>> getAllSorted() {
		List<Rental> rentals = this.rentalRepository.findAll();
		List<GetAllRentalsResponse> response = rentals.stream()
				.map(rental -> this.modelMapperService.forResponse().map(rental, GetAllRentalsResponse.class))
			//.sorted(Comparator.comparing(GetAllRentalsResponse::getTotalPrice).reversed())
				//.sorted(Comparator.comparing(GetAllRentalsResponse::getTotalPrice).reversed()
						//.thenComparing(GetAllRentalsResponse::getTotalDays).reversed())
				.sorted((o1, o2)-> o1.getCityName().compareTo(o2.getCityName()))
			
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllRentalsResponse>>(response, "RENTED.CARS.LISTED");
	}

}


