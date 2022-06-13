package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kodlamaio.rentACar.business.abstracts.CityService;
import com.kodlamaio.rentACar.business.requests.cities.CreateCityRequest;
import com.kodlamaio.rentACar.business.requests.cities.DeleteCityRequest;
import com.kodlamaio.rentACar.business.requests.cities.UpdateCityRequest;
import com.kodlamaio.rentACar.business.responses.cities.GetAllCitiesResponse;
import com.kodlamaio.rentACar.business.responses.cities.GetCityResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;


@RestController
@RequestMapping("api/cities")
public class CitiesController {
	
	private CityService cityService;

	public CitiesController(CityService cityService) {
		this.cityService = cityService;
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateCityRequest createCityRequest) {
		return this.cityService.add(createCityRequest);
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteCityRequest deleteCityRequest) {
		return this.cityService.delete(deleteCityRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody UpdateCityRequest updateCityRequest) {
		return this.cityService.update(updateCityRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllCitiesResponse>> getall() {
		return this.cityService.getAll();
	}
	
	@GetMapping("/getbyid") 
	public DataResult<GetCityResponse> getById(@RequestParam int id) {
		return this.cityService.getById(id);
	}

}
