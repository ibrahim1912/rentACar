package com.kodlamaio.rentACar.business.concretes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.BrandService;
import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.abstracts.ColorService;
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
import com.kodlamaio.rentACar.dataAccess.abstracts.BrandRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.ColorRepository;
import com.kodlamaio.rentACar.entities.concretes.Brand;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Color;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class CarManager implements CarService {


	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	private BrandService brandService;
	private ColorService colorService;
	
	@Autowired
	public CarManager(CarRepository carRepository, ModelMapperService modelMapperService,
			BrandService brandService,ColorService colorService) {

		this.carRepository = carRepository;
		this.modelMapperService = modelMapperService;
		this.brandService = brandService;
		this.colorService = colorService;
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) {
		checkIfBrandLimitExceed(createCarRequest.getBrandId());
		checkIfBrandIdExists(createCarRequest.getBrandId());
		checkIfColorIdExists(createCarRequest.getColorId());
		checkIfCarPlateIsExists(createCarRequest.getPlate());
		
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
		
		return new SuccessDataResult<List<GetAllCarsResponse>>(response,"CARS.LISTED");
	}


	@Override
	public DataResult<GetCarResponse> getById(int id) {
		checkIfCarIdExists(id);
		
		Car car = this.carRepository.findById(id);
		GetCarResponse response = this.modelMapperService.forResponse().map(car, GetCarResponse.class);
		return new SuccessDataResult<GetCarResponse>(response);
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		checkIfCarIdExists(deleteCarRequest.getId());
		this.carRepository.deleteById(deleteCarRequest.getId());
		return new SuccessResult("CAR.DELETED");

	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		
		checkIfCarIdExists(updateCarRequest.getId());
		checkIfBrandIdExists(updateCarRequest.getBrandId());
		checkIfColorIdExists(updateCarRequest.getColorId());  
		checkIfBrandIdSame(updateCarRequest.getId(), updateCarRequest.getBrandId()); 
		checkIfCarPlateIsSame(updateCarRequest.getId(),updateCarRequest);
		
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carRepository.save(car);
		return new SuccessResult("CAR.UPDATED");

	}
	
	@Override
	public Car getByCarId(int carId) {
		checkIfCarIdExists(carId);
		Car car = this.carRepository.findById(carId);
		return car;
	}

	/*************************************************************************************************/
	
	
	@Override
	public DataResult<List<GetAllCarsResponse>> getAllSortedByFindeksNumber() {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.sorted(Comparator.comparing(GetAllCarsResponse::getMinFindeksScore).reversed())
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getAllSortedByFindeksNumberAndKilometer() {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.sorted(Comparator.comparing(GetAllCarsResponse::getMinFindeksScore)
						.thenComparing(GetAllCarsResponse::getKilometer).reversed())
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getByState(int state) {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.filter(car -> car.getState() == state)
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getByCityName(String cityName) {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.filter(car -> cityName.equals(car.getCityName())).collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getByBrandAndColorName(String brandName, String colorName) {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.filter(car -> brandName.equals(car.getBrandName()) && colorName.equals(car.getColorName()))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}

	@Override
	public DataResult<List<GetAllCarsResponse>> getDailyPriceGreaterThan(double dailyPrice) {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.filter(car -> car.getDailyPrice() > dailyPrice)
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCarsResponse>>(response, "CARS.LISTED");
	}
	
	

/*************************************************************************************************/

	private void checkIfBrandLimitExceed(int brandId) {
		List<Car> result = carRepository.findByBrandId(brandId);
		if (result.size() > 10) {
			throw new BusinessException("NO.MORE.BRANDS.CAN.BE.ADDED");
		}
	}

	private void checkIfCarIdExists(int id) {
		Car car = this.carRepository.findById(id);
		if (car == null) {
			throw new BusinessException("THERE.IS.NO.CAR");
		}
	}
	
	private void checkIfBrandIdSame(int carId,int brandId) { 
		Car car = this.carRepository.findById(carId);
		Brand brand = this.brandService.getByBrandId(brandId);
		
		if(car.getBrand().getId() != brand.getId()) {
			checkIfBrandLimitExceed(brandId);
		}
	}
	
	private void checkIfBrandIdExists(int brandId) {
		Brand brand = this.brandService.getByBrandId(brandId);
		if(brand == null) {
			throw new BusinessException("THERE.IS.NOT.BRAND");
		}
	}
	
	private void checkIfColorIdExists(int colorId) {
		Color color = this.colorService.getByColorId(colorId);
		if(color==null) {
			throw new BusinessException("THERE.IS.NOT.COLOR");
		}
	}
	
	private void checkIfCarPlateIsExists(String plate) {
		Car car = this.carRepository.findByPlate(plate);
		if(car != null) {
			throw new BusinessException("CAR.PLATE.EXISTS");
		}
	}
	
	private void checkIfCarPlateIsSame(int carId,UpdateCarRequest updateCarRequest) {
		Car car = this.carRepository.findById(carId);
		if(!car.getPlate().equals(updateCarRequest.getPlate())) {
			checkIfCarPlateIsExists(updateCarRequest.getPlate());
		}
	}

	


}
