package com.kodlamaio.rentACar.business.abstracts;

import java.rmi.RemoteException;
import java.util.List;

import com.kodlamaio.rentACar.business.requests.individualCustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.DeleteIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.UpdateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetAllIndividualCustomersResponse;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetIndividualCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;

public interface IndividualCustomerService {

	Result add (CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NumberFormatException, RemoteException;
	Result delete (DeleteIndividualCustomerRequest deleteIndividualCustomerRequest);
	Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws NumberFormatException, RemoteException;
	DataResult<List<GetAllIndividualCustomersResponse>> getAll();
	DataResult<GetIndividualCustomerResponse> getById(int id);
	DataResult<List<GetAllIndividualCustomersResponse>> getAll(Integer pageNo, Integer pageSize);
	
	IndividualCustomer getByIndividualCustomerId(int individualCustomerId);
}
