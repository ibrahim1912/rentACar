package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureServiceService;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.CreateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.DeleteAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.UpdateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAdditionalFeatureServiceResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAllAdditionalFeatureServicesResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("api/additionalfeatureservices")
public class AdditionalFeatureServicesController {

	private AdditionalFeatureServiceService additionalFeatureServiceService;

	@Autowired
	public AdditionalFeatureServicesController(AdditionalFeatureServiceService additionalFeatureServiceService) {
		this.additionalFeatureServiceService = additionalFeatureServiceService;
	} 
	
	@PostMapping("/add")
	public Result add(@RequestBody CreateAdditionalFeatureServiceRequest createAdditionalFeatureServiceRequest) {
		return this.additionalFeatureServiceService.add(createAdditionalFeatureServiceRequest);
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteAdditionalFeatureServiceRequest deleteAdditionalFeatureServiceRequest) {
		return this.additionalFeatureServiceService.delete(deleteAdditionalFeatureServiceRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody UpdateAdditionalFeatureServiceRequest updateAdditionalFeatureServiceRequest) {
		return this.additionalFeatureServiceService.update(updateAdditionalFeatureServiceRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllAdditionalFeatureServicesResponse>> getAll(){
		return this.additionalFeatureServiceService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetAdditionalFeatureServiceResponse> getById(@RequestParam int id){
		return this.additionalFeatureServiceService.getById(id);
	}
}