package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private CarRepository carRepository;
	private ModelMapperService modelMapperService;

	@Autowired
	public MaintenanceManager(MaintenanceRepository maintenanceRepository, CarRepository carRepository,
			ModelMapperService modelMapperService) {

		this.maintenanceRepository = maintenanceRepository;
		this.carRepository = carRepository;
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

		Car car = this.carRepository.findById(createMaintenanceRequest.getCarId());
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
		checkIfDatesAreCorrect(updateMaintenanceRequest.getSentDate(),updateMaintenanceRequest.getReturnedDate());
		
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

	@Override
	public Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest) {
		checkIfCarIdIsExists(updateMaintenanceRequest.getCarId());
		
		Car car = this.carRepository.findById(updateMaintenanceRequest.getCarId());
		if (car.getState() == 1) {
			car.setState(2);
		} else {
			car.setState(1);
		}
		this.carRepository.save(car);
		return new SuccessResult();
	}

	
	private void checkIfCarIsMaintenance(int carId) {
		Car car = this.carRepository.findById(carId);
		if (car.getState() == 2) {
			throw new BusinessException("CAR.IS.MAINTENANCED");
		}
	}
	
	private void checkIfCarIsRented(int carId) {
		Car car = this.carRepository.findById(carId);
		if (car.getState() == 3) {
			throw new BusinessException("CAR.IS.RENTED");
		}
	}
	
	private void checkIfCarIdIsExists(int carId) { 
		Car car = this.carRepository.findById(carId);
		if(car == null) {
			throw new BusinessException("THERE.IS.NOT.CAR"); 
		}
	}

	private void checkIfMaintenanceIdExists(int maintenanceId) { 
		Maintenance maintenance = this.maintenanceRepository.findById(maintenanceId);
		if(maintenance ==  null) {
			throw new BusinessException("THERE.IS.NOT.MAINTENANCE"); 
		}
	}
	
	private void checkIfDatesAreCorrect(LocalDate sentDate, LocalDate returnedDate) {
		if (!sentDate.isBefore(returnedDate) || sentDate.isBefore(LocalDate.now())) {
			throw new BusinessException("DATE.ERROR"); //isme bak
		}
	}
	
	private void checkIfCarIdExistsInSame(int maintenanceId,int carId) { 
		Maintenance maintenance = this.maintenanceRepository.findById(maintenanceId);
		if(maintenance.getCar().getId() !=  carId) {
			throw new BusinessException("CAR.NOT.FOUND"); 
		}
	}
	
	
}
