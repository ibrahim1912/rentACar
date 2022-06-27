package com.kodlamaio.rentACar.core.adapters;

import java.rmi.RemoteException;

import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.requests.individualCustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.UpdateIndividualCustomerRequest;

import tr.gov.nvi.tckimlik.WS.KPSPublicSoapProxy;


@Service
public class MernisKpsAdapter implements UserValidationService {

	@Override
	public boolean checkIfRealPerson(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NumberFormatException, RemoteException {
		KPSPublicSoapProxy kpsPublicSoapProxy = new KPSPublicSoapProxy();
		boolean result = kpsPublicSoapProxy.TCKimlikNoDogrula(Long.parseLong(createIndividualCustomerRequest.getIdentityNumber()),
				createIndividualCustomerRequest.getFirstName().toUpperCase(), createIndividualCustomerRequest.getLastName().toUpperCase(), createIndividualCustomerRequest.getBirthDate());
		return result;
	}


	@Override
	public boolean checkIfRealPerson(UpdateIndividualCustomerRequest updateIndividualCustomerRequest)
			throws NumberFormatException, RemoteException {
		KPSPublicSoapProxy kpsPublicSoapProxy = new KPSPublicSoapProxy();
		boolean result = kpsPublicSoapProxy.TCKimlikNoDogrula(Long.parseLong(updateIndividualCustomerRequest.getIdentityNumber()),
				updateIndividualCustomerRequest.getFirstName().toUpperCase(), updateIndividualCustomerRequest.getLastName().toUpperCase(), updateIndividualCustomerRequest.getBirthDate());
		return result;
	}


}
