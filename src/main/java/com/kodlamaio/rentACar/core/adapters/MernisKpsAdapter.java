package com.kodlamaio.rentACar.core.adapters;

import java.rmi.RemoteException;

import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.requests.users.CreateUserRequest;
import com.kodlamaio.rentACar.entities.concretes.User;

import tr.gov.nvi.tckimlik.WS.KPSPublicSoapProxy;


@Service
public class MernisKpsAdapter implements UserValidationService {

	@Override
	public boolean checkIfRealPerson(CreateUserRequest user) throws NumberFormatException, RemoteException {
		KPSPublicSoapProxy kpsPublicSoapProxy = new KPSPublicSoapProxy();
		boolean result = kpsPublicSoapProxy.TCKimlikNoDogrula(Long.parseLong(user.getIdentityNumber()),
				user.getFirstName().toUpperCase(), user.getLastName().toUpperCase(), user.getBirthDate().getYear());
		return result;
	}


}
