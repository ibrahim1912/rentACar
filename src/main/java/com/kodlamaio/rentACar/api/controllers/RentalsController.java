package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.DeleteRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.UpdateRentalRequest;
import com.kodlamaio.rentACar.business.responses.rentals.GetAllRentalsResponse;
import com.kodlamaio.rentACar.business.responses.rentals.GetRentalResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@RestController
@RequestMapping("/api/rentals")
public class RentalsController {

	private RentalService rentalService;

	public RentalsController(RentalService rentalService) {
		this.rentalService = rentalService;
	}
	
	@PostMapping("/addforindividualcustomer")
	public Result addForIndividualCustomers(@RequestBody CreateRentalRequest createRentalRequest) {
		return this.rentalService.addForIndividualCustomer(createRentalRequest);
	}
	
	@PostMapping("/addforcorporatecustomer")
	public Result addForCorporateCustomers(@RequestBody CreateRentalRequest createRentalRequest) {
		return this.rentalService.addForCorporateCustomer(createRentalRequest);
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteRentalRequest deleteRentalRequest) {
		return this.rentalService.delete(deleteRentalRequest);
	}
	
	@PostMapping("/updateforindividualcustomer")
	public Result updateForIndividualCustomer(@RequestBody UpdateRentalRequest updateRentalRequest) {
		return this.rentalService.updateForIndividualCustomer(updateRentalRequest);
	}
	@PostMapping("/updateforcorporatecustomer")
	public Result updateForCorporateCustomer(@RequestBody UpdateRentalRequest updateRentalRequest) {
		return this.rentalService.updateForCorporateCustomer(updateRentalRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllRentalsResponse>> getAll() {
		return this.rentalService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetRentalResponse> getById(@RequestParam int id) {
		return this.rentalService.getById(id);
	}
}
