package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.abstracts.MaintenanceService;
import com.kodlamaio.rentACar.business.requests.maintenances.CreateMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.DeleteMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.UpdateMaintenanceRequest;
import com.kodlamaio.rentACar.business.responses.maintenances.GetAllMaintenancesResponse;
import com.kodlamaio.rentACar.business.responses.maintenances.GetMaintenanceResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.ErrorResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.MaintenanceRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Maintenance;

@Service
public class MaintenanceManager implements MaintenanceService {

	private MaintenanceRepository maintenanceRepository;
	private CarService carService;
	private ModelMapperService modelMapperService;

	@Autowired
	public MaintenanceManager(MaintenanceRepository maintenanceRepository, CarService carService,
			ModelMapperService modelMapperService) {

		this.maintenanceRepository = maintenanceRepository;
		this.carService = carService;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateMaintenanceRequest createMaintenanceRequest) {

		checkIfCarIdIsExists(createMaintenanceRequest.getCarId());
		checkIfCarIsMaintenance(createMaintenanceRequest.getCarId());
		checkIfCarIsRented(createMaintenanceRequest.getCarId());
		checkIfDatesAreCorrect(createMaintenanceRequest.getSentDate(),createMaintenanceRequest.getReturnedDate());

		Maintenance maintenance = this.modelMapperService.forResponse().map(createMaintenanceRequest,
				Maintenance.class);

		Car car = this.carService.getByCarId(createMaintenanceRequest.getCarId());
		car.setState(2);

		this.maintenanceRepository.save(maintenance);
		return new SuccessResult("CAR.IS.IN.MAINTENANCE");

	}

	@Override
	public DataResult<List<GetAllMaintenancesResponse>> getAll() {
		List<Maintenance> maintenances = this.maintenanceRepository.findAll();
		List<GetAllMaintenancesResponse> response = maintenances.stream().map(
				maintenance -> this.modelMapperService.forResponse().map(maintenance, GetAllMaintenancesResponse.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllMaintenancesResponse>>(response, "CARS.LISTED");
	}

	@Override
	public Result update(UpdateMaintenanceRequest updateMaintenanceRequest) {
		checkIfMaintenanceIdExists(updateMaintenanceRequest.getId());
		checkIfDatesAreCorrect(updateMaintenanceRequest.getSentDate(), updateMaintenanceRequest.getReturnedDate());
		checkCarChangeInUpdate(updateMaintenanceRequest);

		Maintenance maintenance = this.modelMapperService.forRequest().map(updateMaintenanceRequest, Maintenance.class);
		this.maintenanceRepository.save(maintenance);
		return new SuccessResult("MAINTENANCE.UPDATED");
	}

	@Override
	public Result delete(DeleteMaintenanceRequest deleteMaintenanceRequest) {
		checkIfMaintenanceIdExists(deleteMaintenanceRequest.getId());

		this.maintenanceRepository.deleteById(deleteMaintenanceRequest.getId());
		return new SuccessResult("MAINTENANCE.DELETED");
	}

	@Override
	public DataResult<GetMaintenanceResponse> getById(int id) {
		checkIfMaintenanceIdExists(id);

		Maintenance maintanance = this.maintenanceRepository.findById(id);
		GetMaintenanceResponse response = this.modelMapperService.forResponse().map(maintanance,
				GetMaintenanceResponse.class);
		return new SuccessDataResult<GetMaintenanceResponse>(response);
	}

	

	private void checkIfCarIsMaintenance(int carId) {
		Car car = this.carService.getByCarId(carId);
		if (car.getState() == 2) {
			throw new BusinessException("CAR.HAS.ALREADY.BEEN.MAINTENANCED");
		}
	}

	private void checkIfCarIsRented(int carId) {
		Car car = this.carService.getByCarId(carId);
		if (car.getState() == 3) {
			throw new BusinessException("CAR.HAS.ALREADY.BEEN.RENTED");
		}
	}
	
	private void checkCarState(int carId) {
		Car car = this.carService.getByCarId(carId);
		if (car.getState() != 1) {
			throw new BusinessException("CAR.IS.NOT.AVAILABLE");
		}
	}

	private void checkIfCarIdIsExists(int carId) {
		Car car = this.carService.getByCarId(carId);
		if (car == null) {
			throw new BusinessException("THERE.IS.NOT.CAR");
		}
	}

	private void checkIfMaintenanceIdExists(int maintenanceId) {
		Maintenance maintenance = this.maintenanceRepository.findById(maintenanceId);
		if (maintenance == null) {
			throw new BusinessException("THERE.IS.NOT.MAINTENANCE");
		}
	}

	private void checkIfDatesAreCorrect(LocalDate sentDate, LocalDate returnedDate) {
		if (!sentDate.isBefore(returnedDate) || sentDate.isBefore(LocalDate.now())) {
			throw new BusinessException("DATE.ERROR"); 
		}
	}
	
	private void changeState(UpdateMaintenanceRequest updateMaintenanceRequest) {
		Car car = this.carService.getByCarId(updateMaintenanceRequest.getCarId());
		if (car.getState() == 1) {
			car.setState(2);
		} else {
			car.setState(1);
		}
	}
	
	private void checkCarChangeInUpdate(UpdateMaintenanceRequest updateMaintenanceRequest) {
		Maintenance maintenance = this.maintenanceRepository.findById(updateMaintenanceRequest.getId());
		Car oldCar = maintenance.getCar(); //burdaki car eski
		
		if(updateMaintenanceRequest.getCarId() != oldCar.getId()) {
			checkCarState(updateMaintenanceRequest.getCarId());
			oldCar.setState(1);
			changeState(updateMaintenanceRequest);
		}
	}
	
	@Override
	public Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest) {
		return null;
	}

}
