package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.requests.cars.CreateCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.DeleteCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.UpdateCarRequest;
import com.kodlamaio.rentACar.business.responses.cars.GetAllCarsResponse;
import com.kodlamaio.rentACar.business.responses.cars.GetCarResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.ErrorResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.entities.concretes.Car;


@Service
public class CarManager implements CarService {

	private CarRepository carRepository;
	private ModelMapperService modelMapperService;

	public CarManager(CarRepository carRepository,ModelMapperService modelMapperService) {
		this.carRepository = carRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) {

		if (!checkIfBrandLimitExceed(createCarRequest.getBrandId())) {
			Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
			car.setState(1);
			this.carRepository.save(car);
			return new SuccessResult("CAR.ADDED");
		}
		return new ErrorResult("CAR.NOT.ADDED");

	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getAll() {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream().map(car->this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response,"CAR.LISTED");
	}

	@Override
	public DataResult<GetCarResponse> getById(int id) {

		Car car = this.carRepository.findById(id);
		GetCarResponse response = this.modelMapperService.forResponse().map(car, GetCarResponse.class);
		return new SuccessDataResult<GetCarResponse>(response,"CAR.LISTED");
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		this.carRepository.deleteById(deleteCarRequest.getId());
		return new SuccessResult("CAR.DELETED");

	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
//		Car car = carRepository.findById(updateCarRequest.getId());
//		car.setDailyPrice(updateCarRequest.getDailyPrice());
//		car.setDescription(updateCarRequest.getDescription());
//
//		Brand brand = new Brand();
//		brand.setId(updateCarRequest.getBrandId());
//		//brand.setName(updateCarRequest.getBrandName());
//		car.setBrand(brand);
//
//		Color color = new Color();
//		color.setId(updateCarRequest.getColorId());
//		//color.setName(updateCarRequest.getColorName());
//		car.setColor(color);
		car.setState(1);
		this.carRepository.save(car);
		return new SuccessResult("CAR.UPDATED");

	}

	private boolean checkIfBrandLimitExceed(int id) {
		List<Car> result = carRepository.getByBrandId(id);
		if (result.size() < 5) {
			return false;
		}
		return true;
	}


	// BİR MARKADAN EN FAZLA 5 ADET OLABİLR
	//arabalar bakıma gönderilmelidir carId var
	//araabalara mevcut km plaka bilgisi
	//

}
