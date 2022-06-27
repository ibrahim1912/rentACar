package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.corporateCustomers.CreateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.DeleteCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.UpdateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetAllCorporateCustomersResponse;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetCorporateCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface CorporateCustomerService {

	public Result add(CreateCorporateCustomerRequest corporateCustomerRequest);
	public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest);
	public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest);
	public DataResult<List<GetAllCorporateCustomersResponse>> getAll();
	public DataResult<GetCorporateCustomerResponse> getById(int id);
}
