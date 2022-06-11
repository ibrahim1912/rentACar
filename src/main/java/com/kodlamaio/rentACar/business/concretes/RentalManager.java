package com.kodlamaio.rentACar.business.concretes;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.DeleteRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.UpdateRentalRequest;
import com.kodlamaio.rentACar.business.responses.rentals.GetAllRentalsResponse;
import com.kodlamaio.rentACar.business.responses.rentals.GetRentalResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.ErrorResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.MaintenanceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private CarRepository carRepository;
	private MaintenanceRepository maintenanceRepository;
	private ModelMapperService modelMapperService;

	@Autowired
	public RentalManager(RentalRepository rentalRepository, CarRepository carRepository,
			MaintenanceRepository maintenanceRepository, ModelMapperService modelMapperService) {
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
		this.maintenanceRepository = maintenanceRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {

		if (checkIfCarState(createRentalRequest.getCarId())) {

			Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
			//rental.setPickUpDate(createRentalRequest.getPickUpDate());
			//rental.setReturnDate(createRentalRequest.getReturnDate());
			if (this.checkIfDatesAreCorrect(rental.getPickUpDate(), rental.getReturnDate())) {
				long dayDifference = (rental.getReturnDate().getTime()
						- rental.getPickUpDate().getTime());
				long time = TimeUnit.DAYS.convert(dayDifference, TimeUnit.MILLISECONDS);
				Car car = this.carRepository.findById(createRentalRequest.getCarId());
				car.setState(3);
				rental.setTotalDays((int)time);
				double totalPrice = car.getDailyPrice() * time;
				rental.setTotalPrice(totalPrice);

				this.rentalRepository.save(rental);
				return new SuccessResult("CAR.RENTED");
			}

		}
		return new ErrorResult("CAR.NOT.RENT");

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
		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId());
		Car car = this.carRepository.findById(updateRentalRequest.getCarId());

		rental.setReturnDate(updateRentalRequest.getReturnDate());
		long dayDifference = (rental.getReturnDate().getTime()
				- rental.getPickUpDate().getTime());
		long time = TimeUnit.DAYS.convert(dayDifference, TimeUnit.MILLISECONDS);
		rental.setTotalDays((int)time);
		double totalPrice = car.getDailyPrice() * time;
		rental.setTotalPrice(totalPrice);

		this.rentalRepository.save(rental);
		return new SuccessResult("RENTED.UPDATED");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		this.rentalRepository.deleteById(deleteRentalRequest.getId());
		return new SuccessResult("RENTED.DELETED");
	}

	private boolean checkIfDatesAreCorrect(Date pickUpDate, Date returnDate) {
//		Date date = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");    
//		var x = formatter.format(date);
		if (!pickUpDate.before(returnDate) )  {
			return false;
		} 
		return true;
		
	}

	private boolean checkIfCarState(int id) {
		Car car = this.carRepository.findById(id);
		if (car.getState() == 1) {
			return true;
		}
		return false;

	}

}
