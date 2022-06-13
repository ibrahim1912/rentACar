package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureItemService;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.CreateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.DeleteAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.UpdateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAdditionalFeatureItemResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAllAdditionalFeatureItemsResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;

@RestController // Spring controller oldugunu burdan anlar
@RequestMapping("/api/additionalfeatureitems")
public class AdditionalFeatureItemsController {

	private AdditionalFeatureItemService additionalFeatureItemService;

	public AdditionalFeatureItemsController(AdditionalFeatureItemService additionalFeatureItemService) {
		this.additionalFeatureItemService = additionalFeatureItemService;
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody CreateAdditionalFeatureItemRequest createAdditionalFeatureItemRequest) {
		return this.additionalFeatureItemService.add(createAdditionalFeatureItemRequest);
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteAdditionalFeatureItemRequest deleteAdditionalFeatureItemRequest) {
		return this.additionalFeatureItemService.delete(deleteAdditionalFeatureItemRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody UpdateAdditionalFeatureItemRequest updateAdditionalFeatureItemRequest) {
		return this.additionalFeatureItemService.update(updateAdditionalFeatureItemRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllAdditionalFeatureItemsResponse>>getall() {
		return this.additionalFeatureItemService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetAdditionalFeatureItemResponse> getById(int id) {
		return this.additionalFeatureItemService.getById(id);
	}
	
	
}
