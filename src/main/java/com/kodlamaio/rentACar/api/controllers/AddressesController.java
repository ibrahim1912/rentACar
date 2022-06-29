package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.AddressService;
import com.kodlamaio.rentACar.business.requests.addresses.CreateAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.DeleteAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.UpdateAddressRequest;
import com.kodlamaio.rentACar.business.responses.addresses.GetAddressResponse;
import com.kodlamaio.rentACar.business.responses.addresses.GetAllAddressesResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/addresses")
public class AddressesController {
	

	private AddressService addressService;
	
	@Autowired
	public AddressesController(AddressService addressService) {
		this.addressService = addressService;
	}

	@PostMapping("/addforindividualcustomer")
	public Result addForIndividualCustomer(@RequestBody CreateAddressRequest createAddressRequest) {
		return this.addressService.addForIndividualCustomer(createAddressRequest);
	}
	
	@PostMapping("/addforcorporatecustomer")
	public Result addForCorporateCustomer(@RequestBody CreateAddressRequest createAddressRequest) {
		return this.addressService.addForCorporateCustomer(createAddressRequest);
	}
	
	@PostMapping("/addinvoiceaddressifdifferentforindividualcustomer")
	public Result addInvoiceAddressIfDifferentForIndividualCustomer(@RequestBody CreateAddressRequest createAddressRequest) {
		return this.addressService.addInvoiceAddressIfDifferentForIndividualCustomer(createAddressRequest);
	}
	
	@PostMapping("/addinvoiceaddressifdifferentforcorporatecustomer")
	public Result addInvoiceAddressIfDifferentForCorporateCustomer(@RequestBody CreateAddressRequest createAddressRequest) {
		return this.addressService.addInvoiceAddressIfDifferentForCorporateCustomer(createAddressRequest);
	}
	
	@PostMapping("/updateforindividualcustomer")
	public Result updateForIndividualCustomer(@RequestBody UpdateAddressRequest updateAddressRequest) {
		return this.addressService.updateForIndividualCustomer(updateAddressRequest);
	}
	
	@PostMapping("/updateforcorporatecustomer")
	public Result updateForCorporateCustomer(@RequestBody UpdateAddressRequest updateAddressRequest) {
		return this.addressService.updateForCorporateCustomer(updateAddressRequest);
	}
	
	@PostMapping("/updateifbotharesameforindividualcustomer")
	public Result updateIfBothAreSameForIndividualCustomer(@RequestBody UpdateAddressRequest updateAddressRequest) {
		return this.addressService.updateIfBothAreSameForIndividualCustomer(updateAddressRequest);
	}
	
	@PostMapping("/updateifbotharesameforcorporatecustomer")
	public Result updateIfBothAreSameForCorporateCustomer(@RequestBody UpdateAddressRequest updateAddressRequest) {
		return this.addressService.updateIfBothAreSameForCorporateCustomer(updateAddressRequest);
	}
	
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteAddressRequest deleteAddressRequest) {
		return this.addressService.delete(deleteAddressRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllAddressesResponse>> getAll() {
		return this.addressService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetAddressResponse> getById(@RequestParam int id) {
		return this.addressService.getById(id);
	}
	

}
