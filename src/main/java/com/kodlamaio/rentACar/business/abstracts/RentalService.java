package com.kodlamaio.rentACar.business.abstracts;


import java.util.List;

import com.kodlamaio.rentACar.business.requests.rentals.CreateRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.DeleteRentalRequest;
import com.kodlamaio.rentACar.business.requests.rentals.UpdateRentalRequest;
import com.kodlamaio.rentACar.business.responses.rentals.GetAllRentalsResponse;
import com.kodlamaio.rentACar.business.responses.rentals.GetRentalResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.Rental;


public interface RentalService {

	Result addForIndividualCustomer(CreateRentalRequest createRentalRequest);
	Result addForCorporateCustomer(CreateRentalRequest createRentalRequest);
	Result updateForIndividualCustomer(UpdateRentalRequest updateRentalRequest);
	Result updateForCorporateCustomer(UpdateRentalRequest updateRentalRequest);
	Result delete(DeleteRentalRequest deleteRentalRequest);
	
	DataResult<List<GetAllRentalsResponse>> getAll();
	DataResult<GetRentalResponse> getById(int id);
	
	public Rental getByRentalId(int rentalId);
	
	
	
}
