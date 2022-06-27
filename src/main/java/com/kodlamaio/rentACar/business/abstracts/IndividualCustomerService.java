package com.kodlamaio.rentACar.business.abstracts;

import java.rmi.RemoteException;
import java.util.List;

import com.kodlamaio.rentACar.business.requests.individualcustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualcustomers.DeleteIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualcustomers.UpdateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.responses.individualcustomers.GetAllIndividualCustomersResponse;
import com.kodlamaio.rentACar.business.responses.individualcustomers.GetIndividualCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface IndividualCustomerService {

	public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NumberFormatException, RemoteException;
	public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest);
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws NumberFormatException, RemoteException;
	public DataResult<List<GetAllIndividualCustomersResponse>> getAll();
	public DataResult<GetIndividualCustomerResponse> getById(int id);
	DataResult<List<GetAllIndividualCustomersResponse>> getAll(Integer pageNo, Integer pageSize);
}
