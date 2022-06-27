package com.kodlamaio.rentACar.api.controllers;

import java.rmi.RemoteException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.CorporateCustomerService;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.CreateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.DeleteCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.UpdateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetAllCorporateCustomersResponse;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetCorporateCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/corporatecustomers")
public class CorporateCustomersController {

	private CorporateCustomerService corporateCustomerService;

	public CorporateCustomersController(CorporateCustomerService corporateCustomerService) {
		
		this.corporateCustomerService = corporateCustomerService;
	}
	
	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateCorporateCustomerRequest createCorporateCustomerRequest) throws NumberFormatException, RemoteException {
		return this.corporateCustomerService.add(createCorporateCustomerRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody @Valid UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws NumberFormatException, RemoteException {
		return this.corporateCustomerService.update(updateCorporateCustomerRequest);
	}
	
	@PostMapping("delete")
	public Result delete(@RequestBody  DeleteCorporateCustomerRequest deleteCorporateCustomerRequest)  {
		return this.corporateCustomerService.delete(deleteCorporateCustomerRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllCorporateCustomersResponse>> getAll()  {
		return this.corporateCustomerService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetCorporateCustomerResponse> getById(@RequestParam int id) {
		return this.corporateCustomerService.getById(id);
	}
	
}
