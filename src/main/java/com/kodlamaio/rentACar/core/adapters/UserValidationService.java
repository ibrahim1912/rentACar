package com.kodlamaio.rentACar.core.adapters;

import java.rmi.RemoteException;

import com.kodlamaio.rentACar.business.requests.individualcustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualcustomers.UpdateIndividualCustomerRequest;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;

public interface UserValidationService {

	boolean checkIfRealPerson(CreateIndividualCustomerRequest createIndividualCustomerRequest)throws NumberFormatException, RemoteException;
	boolean checkIfRealPerson(UpdateIndividualCustomerRequest updateIndividualCustomerRequest)throws NumberFormatException, RemoteException;
}
