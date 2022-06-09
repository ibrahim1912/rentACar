package com.kodlamaio.rentACar.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
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
	
	@Autowired
	public RentalManager(RentalRepository rentalRepository, CarRepository carRepository,MaintenanceRepository maintenanceRepository ) {
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
		this.maintenanceRepository = maintenanceRepository;
	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {
		if(checkIfCarInMaintanance(createRentalRequest.getCarId())) {
			Rental rental = new Rental();
			rental.setPickUpDate(createRentalRequest.getPickUpDate());
			rental.setReturnDate(createRentalRequest.getReturnDate());
			
			Car car = this.carRepository.findById(createRentalRequest.getCarId());
			car.setState(3);
			
			int dayDifference = (rental.getReturnDate().getDate() - rental.getPickUpDate().getDay());
			rental.setTotalDays(dayDifference);
			
			int totalPrice = (int)car.getDailyPrice() * dayDifference;
			rental.setTotalPrice(totalPrice);
			
			this.rentalRepository.save(rental);
			
			return new SuccessResult("CAR.RENTED");
		}
		
		return new ErrorResult("CAR.NOT.RENT");
		
		
	}

	@Override
	public DataResult<List<Rental>> getAll() {
		
		return new SuccessDataResult<List<Rental>>(this.rentalRepository.findAll());
	}
	
	private boolean checkIfCarInMaintanance(int id) {
		
		if( this.carRepository.findById(id).getState() == 2) {
			return false;
		}
		return true;
	}

}
