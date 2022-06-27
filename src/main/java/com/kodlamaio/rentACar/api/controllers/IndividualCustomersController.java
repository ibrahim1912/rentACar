package com.kodlamaio.rentACar.api.controllers;

import java.rmi.RemoteException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.IndividualCustomerService;
import com.kodlamaio.rentACar.business.requests.individualCustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.DeleteIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.UpdateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetAllIndividualCustomersResponse;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetIndividualCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/individualcustomers")
public class IndividualCustomersController {
	private IndividualCustomerService individualCustomerService;

	@Autowired
	public IndividualCustomersController(IndividualCustomerService individualCustomerService) {
		this.individualCustomerService = individualCustomerService;
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NumberFormatException, RemoteException {
		return this.individualCustomerService.add(createIndividualCustomerRequest);
	}
	
	@PostMapping("/delete")
	public Result add(@RequestBody DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {
		return this.individualCustomerService.delete(deleteIndividualCustomerRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody @Valid UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws NumberFormatException, RemoteException {
		return this.individualCustomerService.update(updateIndividualCustomerRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllIndividualCustomersResponse>> getAll(){
		return this.individualCustomerService.getAll();
	}
	
	@GetMapping("/getallbypage")
	public DataResult<List<GetAllIndividualCustomersResponse>> getAll(@RequestParam int pageNo, int pageSize){
		return this.individualCustomerService.getAll(pageNo,pageSize);
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetIndividualCustomerResponse> getById(@RequestParam int id){
		return this.individualCustomerService.getById(id);
	}

}
