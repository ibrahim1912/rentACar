package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.OrderedAdditionalItemService;
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
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.CorporateCustomerRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.IndividualCustomerRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.OrderedAdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	private AdditionalItemRepository additionalItemRepository;
	private OrderedAdditionalItemRepository orderedAdditionalItemRepository;
	private FindeksValidationService findeksValidationService;
	private IndividualCustomerRepository individualCustomerRepository;
	private CorporateCustomerRepository corporateCustomerRepository;

	@Autowired
	public RentalManager(RentalRepository rentalRepository, CarRepository carRepository,
			ModelMapperService modelMapperService, AdditionalItemRepository additionalItemRepository,
			OrderedAdditionalItemRepository orderedAdditionalItemRepository,
			FindeksValidationService findeksValidationService,
			IndividualCustomerRepository individualCustomerRepository,
			CorporateCustomerRepository corporateCustomerRepository) {
	
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
		this.modelMapperService = modelMapperService;
		this.additionalItemRepository = additionalItemRepository;
		this.orderedAdditionalItemRepository = orderedAdditionalItemRepository;
		this.findeksValidationService = findeksValidationService;
		this.individualCustomerRepository = individualCustomerRepository;
		this.corporateCustomerRepository = corporateCustomerRepository;
	}

	@Override
	public Result addForIndividualCustomers(CreateRentalRequest createRentalRequest) {
		
		checkIfCarExists(createRentalRequest.getCarId());
		checkIfCarState(createRentalRequest.getCarId());
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());
		checkUserFindexScoreAndCarFindexScore(createRentalRequest.getCarId(),createRentalRequest.getCustomerId());
		checkIfIndividualPersonExists(createRentalRequest.getCustomerId());
	
		Car car = this.carRepository.findById(createRentalRequest.getCarId());
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		
		car.setState(3); //araba kiralanmış
		//rental.setPickUpCityId(car.getCity());
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		//rental.setPickUpCityId(car.getCity());
		car.setCity(rental.getReturnCityId());
		this.rentalRepository.save(rental);
		return new SuccessResult("CAR.RENTED");
	}
	
	@Override
	public Result addForCorporateCustomers(CreateRentalRequest createRentalRequest) {
		checkIfCarExists(createRentalRequest.getCarId());
		checkIfCarState(createRentalRequest.getCarId());
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());
		checkIfCorporateCustomerIdExists(createRentalRequest.getCustomerId());
		
		Car car = this.carRepository.findById(createRentalRequest.getCarId());
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		car.setState(3); //araba kiralanmış
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
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
		checkIfRentalIdExists(id);
		
		Rental rental = this.rentalRepository.findById(id);
		GetRentalResponse response = this.modelMapperService.forResponse().map(rental, GetRentalResponse.class);

		return new SuccessDataResult<GetRentalResponse>(response);
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		checkIfRentalIdExists(updateRentalRequest.getId());
		checkIfCarExists(updateRentalRequest.getCarId());
		
		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId());
		Rental rentalMapped = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		Car car = this.carRepository.findById(updateRentalRequest.getCarId());
		
		rental.setReturnDate(updateRentalRequest.getReturnDate());
		rental.setReturnCityId(rentalMapped.getReturnCityId());
		car.setCity(rentalMapped.getReturnCityId());
		//arabanın state ni yaz
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		this.rentalRepository.save(rental);
		
		return new SuccessResult("RENTED.UPDATED");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		checkIfRentalIdExists(deleteRentalRequest.getId());
		
		this.rentalRepository.deleteById(deleteRentalRequest.getId());
		return new SuccessResult("RENTED.DELETED");
	}

	private void checkIfCarExists(int carId) {
		Car car = this.carRepository.findById(carId);
		if(car == null) {
			throw new BusinessException("SUCH.A.CAR.DOES.NOT.EXISTS");
		}
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
	
	private void checkIfRentalIdExists(int rentalId) {
		Rental rental = this.rentalRepository.findById(rentalId);
		if(rental == null) {
			throw new BusinessException("THERE.IS.NO.RENTED.CAR");
		}
	}
	
	private void checkUserFindexScoreAndCarFindexScore(int carId, int individualId) {
		checkIfIndividualPersonExists(individualId);
		Car car = this.carRepository.findById(carId);
		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByIndividualCustomerId(individualId);
		int userFindexScore = this.findeksValidationService.calculateFindeksScoreOfUser(individualCustomer.getIdentityNumber());
		System.out.println("Car score "+car.getMinFindeksScore());
		if(userFindexScore < car.getMinFindeksScore()) {
			throw new BusinessException("FINDEKS.SCORE.IS.NOT.ENOUGH");
		}
		
	}

	private void checkIfIndividualPersonExists(int individualCustomerId) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByIndividualCustomerId(individualCustomerId);
		if(individualCustomer == null) {
			throw new BusinessException("THERE.IS.NOT.A.INDIVIDUAL.CUSTOMER");
		}
	}
	
	private void checkIfCorporateCustomerIdExists(int corporateCustomerId) {
		CorporateCustomer corporateCustomer = this.corporateCustomerRepository.findByCorporateCustomerId(corporateCustomerId);
		if(corporateCustomer == null) {
			throw new BusinessException("THERE.IS.NOT.A.CORPORATE.CUSTOMER");
		}
	}



}


