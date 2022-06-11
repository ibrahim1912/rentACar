package com.kodlamaio.rentACar.business.concretes;

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
		if (checkIfCarState(createMaintenanceRequest.getCarId())) {
			Maintenance maintenance = this.modelMapperService.forResponse().map(createMaintenanceRequest,
					Maintenance.class);

			Car car = this.carRepository.findById(createMaintenanceRequest.getCarId());
			car.setId(createMaintenanceRequest.getCarId());
			car.setState(2);
			maintenance.setCar(car);

			this.maintenanceRepository.save(maintenance);
			return new SuccessResult("CAR.IS.IN.MAINTENANCE");
		}
		return new ErrorResult("CAR.IS.NOT.IN.MAINTENANCE");

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
		Maintenance maintenance = this.modelMapperService.forRequest()
				.map(updateMaintenanceRequest, Maintenance.class);
		Maintenance miantenanceFromDatabase = this.maintenanceRepository.findById(updateMaintenanceRequest.getId());
		maintenance.setDatesent(miantenanceFromDatabase.getDatesent());
		this.maintenanceRepository.save(maintenance);
		return new SuccessResult();
	}

	@Override
	public Result delete(DeleteMaintenanceRequest deleteMaintenanceRequest) {
		this.maintenanceRepository.deleteById(deleteMaintenanceRequest.getId());
		return new SuccessResult("MAINTENANCE.DELETED");
	}

	@Override
	public DataResult<GetMaintenanceResponse> getById(int id) {
		Maintenance maintance = this.maintenanceRepository.findById(id);
		GetMaintenanceResponse response = this.modelMapperService.forResponse().map(maintance,
				GetMaintenanceResponse.class);
		return new SuccessDataResult<GetMaintenanceResponse>(response);
	}

	@Override
	public Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest) {
		Car car = this.carRepository.findById(updateMaintenanceRequest.getCarId());
		if (car.getState() == 1) {
			car.setState(2);
		} else {
			car.setState(1);
		}
		this.carRepository.save(car);
		return new SuccessResult();
	}

	private boolean checkIfCarState(int id) {
		Car car = this.carRepository.findById(id);
		if (car.getState() == 1) {
			return true;
		}
		return false;

	}
}
