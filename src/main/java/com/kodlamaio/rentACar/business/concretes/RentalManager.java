package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureServiceService;
import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.DeleteRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.UpdateRentalRequest;
import com.kodlamaio.rentACar.business.responses.rentals.GetAllRentalsResponse;
import com.kodlamaio.rentACar.business.responses.rentals.GetRentalResponse;
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
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Rental;

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
	public RentalManager(RentalRepository rentalRepository, CarRepository carRepository,
			ModelMapperService modelMapperService) {
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
		this.modelMapperService = modelMapperService;

	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {

		checkIfCarState(createRentalRequest.getCarId());
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());

		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		Car car = this.carRepository.findById(createRentalRequest.getCarId());
		car.setState(3);
		rental.setPickUpCityId(car.getCity());
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		rental.setPickUpCityId(car.getCity());
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
//		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId());
//		Car car = this.carRepository.findById(updateRentalRequest.getCarId());
//
//		rental.setReturnDate(updateRentalRequest.getReturnDate());
//		long dayDifference = (rental.getReturnDate().getTime() - rental.getPickUpDate().getTime());
//		long time = TimeUnit.DAYS.convert(dayDifference, TimeUnit.MILLISECONDS);
//		rental.setTotalDays((int) time);
//		double totalPrice = car.getDailyPrice() * time;
//		
//		
//		rental.setTotalPrice(totalPrice);
//		car.setCity(rental.getReturnCityId());
//		this.rentalRepository.save(rental);
//		return new SuccessResult("RENTED.UPDATED");
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
//		List<AdditionalFeatureService> additionalFeatureServices = this.additionalFeatureServiceRepository.getByRentalId(rental.getId());
//		double totalAdditionalFeatureServicePrice = 0;
//		
//		for (AdditionalFeatureService additionalFeatureService : additionalFeatureServices) {
//			totalAdditionalFeatureServicePrice += additionalFeatureService.getAdditionalFeatureItem().getPrice();
//		}
		
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

}
