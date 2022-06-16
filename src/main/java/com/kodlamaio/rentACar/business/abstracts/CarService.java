package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.cars.CreateCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.DeleteCarRequest;
import com.kodlamaio.rentACar.business.requests.cars.UpdateCarRequest;
import com.kodlamaio.rentACar.business.responses.cars.GetAllCarsResponse;
import com.kodlamaio.rentACar.business.responses.cars.GetCarResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;



public interface CarService {

	Result add(CreateCarRequest createCarRequest);
	Result delete(DeleteCarRequest deleteCarRequest);
	Result update(UpdateCarRequest updateCarRequest);
	
	DataResult<List<GetAllCarsResponse>> getAll();
	DataResult<GetCarResponse> getById(int id);
	DataResult<List<GetAllCarsResponse>> getAllSorted();
	DataResult<List<GetAllCarsResponse>> getAllSorted2(String cityName);
	
	
	
	DataResult<List<GetAllCarsResponse>> getAllSortedByFindeksNumber();
	DataResult<List<GetAllCarsResponse>> getAllSortedByFindeksNumberAndKilometer();
	DataResult<List<GetAllCarsResponse>> getByState(int state);
	DataResult<List<GetAllCarsResponse>> getByCityName(String cityName);
	DataResult<List<GetAllCarsResponse>> getByBrandAndColorName(String brandName,String colorName);
	DataResult<List<GetAllCarsResponse>> getDailyPriceGreaterThan(double dailyPrice);
	
	
	DataResult<List<GetAllCarsResponse>> getff();
}
