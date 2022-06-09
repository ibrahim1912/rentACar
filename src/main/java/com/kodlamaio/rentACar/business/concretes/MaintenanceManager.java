package com.kodlamaio.rentACar.business.concretes;


import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.abstracts.MaintenanceService;
import com.kodlamaio.rentACar.business.requests.maintenances.CreateMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.DeleteMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.UpdateMaintenanceRequest;
import com.kodlamaio.rentACar.business.responses.maintenances.ReadMaintenanceResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
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
	

	@Autowired
	public MaintenanceManager(MaintenanceRepository maintenanceRepository,CarService carService) {

		this.maintenanceRepository = maintenanceRepository;
		this.carService = carService;
	}

	@Override
	public Result add(CreateMaintenanceRequest createMaintenanceRequest) {
		Maintenance maintenance = new Maintenance();
		maintenance.setDatesent(createMaintenanceRequest.getDatesent());
		maintenance.setDatereturned(createMaintenanceRequest.getDatereturned());
		
		
		//arabayı id ile cek güncelle
		//update de arabanın state ni 1  yap
		Car car = this.carService.getById(createMaintenanceRequest.getCarId());
		car.setId(createMaintenanceRequest.getCarId());
		car.setState(2);
		maintenance.setCar(car);
		
		this.maintenanceRepository.save(maintenance);
		return new SuccessResult("CAR.IS.IN.MAINTENANCE");
	}

	@Override
	public DataResult<List<Maintenance>> getAll() {
		return new SuccessDataResult<List<Maintenance>>(this.maintenanceRepository.findAll());
	}

	@Override
	public Result update(UpdateMaintenanceRequest updateMaintenanceRequest) {
		Maintenance maintenance = this.maintenanceRepository.findById(updateMaintenanceRequest.getId());
		maintenance.setDatereturned(updateMaintenanceRequest.getDatereturned());
	
		Car car = this.carRepository.findById(updateMaintenanceRequest.getCarId());
		car.setId(updateMaintenanceRequest.getCarId());

		maintenance.setCar(car);
		this.maintenanceRepository.save(maintenance);
		return new SuccessResult();
	}

	@Override
	public Result delete(DeleteMaintenanceRequest deleteMaintenanceRequest) {
		this.maintenanceRepository.deleteById(deleteMaintenanceRequest.getId());
		return new SuccessResult("MAINTENANCE.DELETED");
	}

	@Override
	public DataResult<Maintenance> getById(ReadMaintenanceResponse readMaintenanceResponce) {
		return new SuccessDataResult<Maintenance>(this.maintenanceRepository.findById(readMaintenanceResponce.getId()));
	}

	@Override
	public Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest) {
		Car car = this.carRepository.findById(updateMaintenanceRequest.getCarId());
		if(car.getState() == 1) {
			car.setState(2);
		}else {
			car.setState(1);
		}
		this.carRepository.save(car);
		return new SuccessResult();
	}

}
