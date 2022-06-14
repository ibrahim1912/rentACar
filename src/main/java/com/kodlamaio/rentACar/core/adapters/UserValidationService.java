package com.kodlamaio.rentACar.core.adapters;

import java.rmi.RemoteException;

import com.kodlamaio.rentACar.business.requests.users.CreateUserRequest;
import com.kodlamaio.rentACar.entities.concretes.User;

public interface UserValidationService {

	boolean checkIfRealPerson(CreateUserRequest user)throws NumberFormatException, RemoteException;
}
