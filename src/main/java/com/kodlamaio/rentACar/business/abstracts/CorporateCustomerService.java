package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.corporatecustomers.CreateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporatecustomers.DeleteCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporatecustomers.UpdateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.responses.corporatecustomers.GetAllCorporateCustomersResponse;
import com.kodlamaio.rentACar.business.responses.corporatecustomers.GetCorporateCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface CorporateCustomerService {

	public Result add(CreateCorporateCustomerRequest corporateCustomerRequest);
	public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest);
	public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest);
	public DataResult<List<GetAllCorporateCustomersResponse>> getAll();
	public DataResult<GetCorporateCustomerResponse> getById(int id);
}
