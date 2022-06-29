package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.corporateCustomers.CreateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.DeleteCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.UpdateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetAllCorporateCustomersResponse;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetCorporateCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;

public interface CorporateCustomerService {

	Result add(CreateCorporateCustomerRequest corporateCustomerRequest);
	Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest);
	Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest);
	DataResult<List<GetAllCorporateCustomersResponse>> getAll();
	DataResult<GetCorporateCustomerResponse> getById(int id);
	
	CorporateCustomer getByCorporateCustomerId(int corporateCustomerId);
}
