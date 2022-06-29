package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.addresses.CreateAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.DeleteAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.UpdateAddressRequest;
import com.kodlamaio.rentACar.business.responses.addresses.GetAddressResponse;
import com.kodlamaio.rentACar.business.responses.addresses.GetAllAddressesResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface AddressService {

	
	Result addForIndividualCustomer(CreateAddressRequest createAddressRequest);
	Result addForCorporateCustomer(CreateAddressRequest createAddressRequest);
	Result addInvoiceAddressIfDifferentForIndividualCustomer(CreateAddressRequest createAddressRequest);
	Result addInvoiceAddressIfDifferentForCorporateCustomer(CreateAddressRequest createAddressRequest);
	Result delete(DeleteAddressRequest deleddteAddressRequest);
	Result updateForIndividualCustomer(UpdateAddressRequest updateAddressRequest);
	Result updateForCorporateCustomer(UpdateAddressRequest updateAddressRequest);
	Result updateIfBothAreSameForIndividualCustomer(UpdateAddressRequest updateAddressRequest);
	Result updateIfBothAreSameForCorporateCustomer(UpdateAddressRequest updateAddressRequest);

	DataResult<List<GetAllAddressesResponse>> getAll();
	DataResult<GetAddressResponse> getById(int id);
}

