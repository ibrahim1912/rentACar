package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.abstracts.CorporateCustomerService;
import com.kodlamaio.rentACar.business.abstracts.IndividualCustomerService;
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
import com.kodlamaio.rentACar.dataAccess.abstracts.CorporateCustomerRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.IndividualCustomerRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;
import com.kodlamaio.rentACar.entities.concretes.OrderedAdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private CarService carService;
	private ModelMapperService modelMapperService;
	private FindeksValidationService findeksValidationService;
	private IndividualCustomerService individualCustomerService;
	private CorporateCustomerService corporateCustomerService;

	@Autowired
	public RentalManager(RentalRepository rentalRepository,CarService carService,
			ModelMapperService modelMapperService, 
			FindeksValidationService findeksValidationService,
			IndividualCustomerService individualCustomerService,
			CorporateCustomerService corporateCustomerService) {
	
		this.rentalRepository = rentalRepository;
		this.carService = carService;
		this.modelMapperService = modelMapperService;
		this.findeksValidationService = findeksValidationService;
		this.individualCustomerService = individualCustomerService;
		this.individualCustomerService = individualCustomerService;
		
	}

	@Override
	public Result addForIndividualCustomer(CreateRentalRequest createRentalRequest) {
		
		//checkIfCarIdExists(createRentalRequest.getCarId());
		checkIfIndividualPersonExists(createRentalRequest.getCustomerId());
		checkIfCarStateIsAvailable(createRentalRequest.getCarId());
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());
		checkUserFindexScoreAndCarFindexScore(createRentalRequest.getCarId(),createRentalRequest.getCustomerId());
		
		Car car = this.carService.getByCarId(createRentalRequest.getCarId());
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		car.setState(3); //araba kiralanmış
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		rental.setTotalDays(calculateTotalDaysOfRental(rental));
		this.rentalRepository.save(rental);
		return new SuccessResult("CAR.RENTED");
	}
	
	@Override
	public Result addForCorporateCustomer(CreateRentalRequest createRentalRequest) {
		checkIfCarIdExists(createRentalRequest.getCarId());
		checkIfCorporateCustomerIdExists(createRentalRequest.getCustomerId());
		checkIfCarStateIsAvailable(createRentalRequest.getCarId());
		checkIfDatesAreCorrect(createRentalRequest.getPickUpDate(), createRentalRequest.getReturnDate());
		
		Car car = this.carService.getByCarId(createRentalRequest.getCarId());
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		car.setState(3); //araba kiralanmış
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		rental.setTotalDays(calculateTotalDaysOfRental(rental));
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
	public Result updateForIndividualCustomer(UpdateRentalRequest updateRentalRequest) {
		checkIfRentalIdExists(updateRentalRequest.getId());
		//checkIfCarIdExists(updateRentalRequest.getCarId());
		checkIfIndividualPersonExists(updateRentalRequest.getCustomerId());
		checkCarChangeInUpdate(updateRentalRequest);
		
		Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		Car car = this.carService.getByCarId(updateRentalRequest.getCarId());
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		rental.setTotalDays(calculateTotalDaysOfRental(rental));
		this.rentalRepository.save(rental);
		return new SuccessResult("RENTED.UPDATED");
	}
	
	@Override
	public Result updateForCorporateCustomer(UpdateRentalRequest updateRentalRequest) {
		checkIfRentalIdExists(updateRentalRequest.getId());
		//checkIfCarIdExists(updateRentalRequest.getCarId());
		checkIfCorporateCustomerIdExists(updateRentalRequest.getCustomerId());
		checkCarChangeInUpdate(updateRentalRequest);
		
		Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		Car car = this.carService.getByCarId(updateRentalRequest.getCarId());
		rental.setTotalPrice(calculateTotalPriceOfRental(rental, car.getDailyPrice()));
		rental.setTotalDays(calculateTotalDaysOfRental(rental));
		this.rentalRepository.save(rental);
		return new SuccessResult("RENTED.UPDATED");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		checkIfRentalIdExists(deleteRentalRequest.getId());
		
		this.rentalRepository.deleteById(deleteRentalRequest.getId());
		return new SuccessResult("RENTED.DELETED");
	}
	
	@Override
	public Rental getByRentalId(int rentalId) {
		checkIfRentalIdExists(rentalId);
		
		Rental rental = this.rentalRepository.findById(rentalId);
		return rental;
	}

	private void checkIfCarIdExists(int carId) {
		Car car = this.carService.getByCarId(carId);
		if(car == null) {
			throw new BusinessException("SUCH.A.CAR.DOES.NOT.EXISTS");
		}
	}
	
	private void checkIfDatesAreCorrect(LocalDate pickUpDate, LocalDate returnDate) {
		if (!pickUpDate.isBefore(returnDate) || pickUpDate.isBefore(LocalDate.now())) {
			throw new BusinessException("DATE.ERROR"); 
		}
	}

	private double calculateTotalPriceOfRental(Rental rental, double carDailyPrice) {
		double totalPrice = 0;
		int daysDifference = (int) ChronoUnit.DAYS.between(rental.getPickUpDate(), rental.getReturnDate());
		totalPrice = (carDailyPrice * daysDifference) +  
				+ calculatePriceByReturnLocation(rental.getPickUpCityId().getId(), rental.getReturnCityId().getId());
		return totalPrice;
	}
	
	private int calculateTotalDaysOfRental(Rental rental) {
		int daysDifference = (int) ChronoUnit.DAYS.between(rental.getPickUpDate(), rental.getReturnDate());
		return daysDifference;
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
		
		Car car = this.carService.getByCarId(carId);
		IndividualCustomer individualCustomer = this.individualCustomerService.getByIndividualCustomerId(individualId);
		int userFindexScore = this.findeksValidationService.calculateFindeksScoreOfUser(individualCustomer.getIdentityNumber());
		System.out.println("Car score "+car.getMinFindeksScore());
		if(userFindexScore < car.getMinFindeksScore()) {
			throw new BusinessException("FINDEKS.SCORE.IS.NOT.ENOUGH");
		}
		
	}

	private void checkIfIndividualPersonExists(int individualCustomerId) {
		IndividualCustomer individualCustomer = this.individualCustomerService.getByIndividualCustomerId(individualCustomerId);
		if(individualCustomer == null) {
			throw new BusinessException("THERE.IS.NOT.A.INDIVIDUAL.CUSTOMER");
		}
	}
	
	private void checkIfCorporateCustomerIdExists(int corporateCustomerId) {
		CorporateCustomer corporateCustomer = this.corporateCustomerService.getByCorporateCustomerId(corporateCustomerId);
		if(corporateCustomer == null) {
			throw new BusinessException("THERE.IS.NOT.A.CORPORATE.CUSTOMER");
		}
	}

	private void updateCarState(UpdateRentalRequest updateRentalRequest) {
		Car car = this.carService.getByCarId(updateRentalRequest.getCarId());
		if (car.getState() == 1) {
			car.setState(3);
		} else {
			car.setState(1);
		}
	}
	
	private void checkCarChangeInUpdate(UpdateRentalRequest updateRentalRequest) {
		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId());
		Car oldCar = rental.getCar(); //burdaki car eski
		
		if(updateRentalRequest.getCarId() != oldCar.getId()) {
			checkIfCarStateIsAvailable(updateRentalRequest.getCarId());
			oldCar.setState(1);
			updateCarState(updateRentalRequest);
		}
		
	}
	private void checkIfCarStateIsAvailable(int carId) {
		Car car = this.carService.getByCarId(carId);
		if(car.getState() != 1) {
			throw new BusinessException("CAR.IS.NOT.AVAILABLE");
		}
	}

}


