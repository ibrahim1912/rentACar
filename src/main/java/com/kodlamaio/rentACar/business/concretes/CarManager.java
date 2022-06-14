package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.requests.cars.CreateCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.DeleteCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.UpdateCarRequest;
import com.kodlamaio.rentACar.business.responses.cars.GetAllCarsResponse;
import com.kodlamaio.rentACar.business.responses.cars.GetCarResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class CarManager implements CarService {
	
	@Autowired
	private CarRepository carRepository;
	@Autowired
	private ModelMapperService modelMapperService;


	@Override
	public Result add(CreateCarRequest createCarRequest) {

		checkIfBrandLimitExceed(createCarRequest.getBrandId());
		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
		car.setState(1);
		this.carRepository.save(car);
		return new SuccessResult("CAR.ADDED");

	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getAll() {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CAR.LISTED");
	}

	@Override
	public DataResult<GetCarResponse> getById(int id) {

		Car car = this.carRepository.findById(id);
		GetCarResponse response = this.modelMapperService.forResponse().map(car, GetCarResponse.class);
		return new SuccessDataResult<GetCarResponse>(response, "CAR.LISTED");
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		this.carRepository.deleteById(deleteCarRequest.getId());
		return new SuccessResult("CAR.DELETED");

	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		checkIfIdExists(updateCarRequest.getId());
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		Car carFromDb = this.carRepository.findById(car.getId());
		
		car.setState(1);
		this.carRepository.save(car);
		return new SuccessResult("CAR.UPDATED");

	}

	private void checkIfBrandLimitExceed(int id) {
		List<Car> result = carRepository.getByBrandId(id);
		if (result.size() > 5) {
			throw new BusinessException("NO.MORE.BRANDS.CAN.BE.ADDED");
		}
	}
	
	private void checkIfIdExists(int id) {
		Car car = this.carRepository.findById(id);
		if(car == null) {
			throw new BusinessException("THERE.IS.NO.CAR");
		}
	}
	

	// BİR MARKADAN EN FAZLA 5 ADET OLABİLR
	// arabalar bakıma gönderilmelidir carId var
	// araabalara mevcut km plaka bilgisi
	//

}
