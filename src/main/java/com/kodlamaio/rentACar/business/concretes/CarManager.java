package com.kodlamaio.rentACar.business.concretes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CarService;
import com.kodlamaio.rentACar.business.requests.cars.CreateCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.DeleteCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.UpdateCarRequest;
import com.kodlamaio.rentACar.business.responses.cars.ReadCarResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.ErrorResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CarRepository;
import com.kodlamaio.rentACar.entities.concretes.Brand;
import com.kodlamaio.rentACar.entities.concretes.Car;
import com.kodlamaio.rentACar.entities.concretes.Color;

@Service
public class CarManager implements CarService {

	private CarRepository carRepository;

	public CarManager(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) {

		if (!checkIfBrandLimitExceed(createCarRequest.getBrandId())) {
			Car car = new Car();
			car.setDailyPrice(createCarRequest.getDailyPrice());
			car.setDescription(createCarRequest.getDescription());
			car.setPlate(createCarRequest.getPlate());
			car.setKilometer(createCarRequest.getKilometer());
			car.setState(1);
			
			Brand brand = new Brand();
			brand.setId(createCarRequest.getBrandId());
			car.setBrand(brand);

			Color color = new Color();
			color.setId(createCarRequest.getColorId());
			car.setColor(color);

			this.carRepository.save(car);
			return new SuccessResult("CAR.ADDED");
		}

		return new ErrorResult("CAR.NOT.ADDED");

	}

	@Override
	public DataResult<List<Car>> getAll() {

		return new SuccessDataResult<List<Car>>(this.carRepository.findAll());
	}

	@Override
	public DataResult<Car> getById(ReadCarResponse readCarResponse) {

		return new SuccessDataResult<Car>(this.carRepository.getById(readCarResponse.getId()));
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		this.carRepository.deleteById(deleteCarRequest.getId());
		return new SuccessResult("CAR.DELETED");

	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		Car car = carRepository.getById(updateCarRequest.getId());
		car.setDailyPrice(updateCarRequest.getDailyPrice());
		car.setDescription(updateCarRequest.getDescription());

		Brand brand = new Brand();
		brand.setId(updateCarRequest.getBrandId());
		//brand.setName(updateCarRequest.getBrandName());
		car.setBrand(brand);

		Color color = new Color();
		color.setId(updateCarRequest.getColorId());
		//color.setName(updateCarRequest.getColorName());
		car.setColor(color);

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
