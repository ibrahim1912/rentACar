package com.kodlamaio.rentACar.business.concretes;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.IndividualCustomerService;
import com.kodlamaio.rentACar.business.requests.individualCustomers.CreateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.DeleteIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.requests.individualCustomers.UpdateIndividualCustomerRequest;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetAllIndividualCustomersResponse;
import com.kodlamaio.rentACar.business.responses.individualCustomers.GetIndividualCustomerResponse;
import com.kodlamaio.rentACar.core.adapters.UserValidationService;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.IndividualCustomerRepository;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

	private IndividualCustomerRepository individualCustomerRepository;
	private ModelMapperService modelMapperService;
	private UserValidationService userValidationService;

	@Autowired
	public IndividualCustomerManager(IndividualCustomerRepository individualCustomerRepository,
			ModelMapperService modelMapperService, UserValidationService userValidationService) {
		this.individualCustomerRepository = individualCustomerRepository;
		this.modelMapperService = modelMapperService;
		this.userValidationService = userValidationService;
	}

	@Override
	public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest)
			throws NumberFormatException, RemoteException {

		checkIfRealPerson(createIndividualCustomerRequest);
		checkIfIdentityIsSame(createIndividualCustomerRequest.getIdentityNumber());
		checkIfEmailIsSame(createIndividualCustomerRequest.getEmail());

		IndividualCustomer individualCustomer = this.modelMapperService.forRequest()
				.map(createIndividualCustomerRequest, IndividualCustomer.class);
		
		this.individualCustomerRepository.save(individualCustomer);
		return new SuccessResult("INDIVIDUAL.CUSTOMER.ADDED");
	}

	@Override
	public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {
		checkIfIndividualCustomerIdExists(deleteIndividualCustomerRequest.getIndividualCustomerId());

		this.individualCustomerRepository.deleteById(deleteIndividualCustomerRequest.getIndividualCustomerId());
		return new SuccessResult("INDIVIDUAL.CUSTOMER.DELETED");
	}

	@Override
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest)
			throws NumberFormatException, RemoteException {
		
		checkIfIndividualCustomerIdExists(updateIndividualCustomerRequest.getIndividualCustomerId());
		checkIfRealPerson(updateIndividualCustomerRequest);
		checkIfEmailIsSameForUpdate(updateIndividualCustomerRequest.getIndividualCustomerId(),
				updateIndividualCustomerRequest.getEmail());
		checkIfIdentityIsSameForUpdate(updateIndividualCustomerRequest.getIndividualCustomerId(),
				updateIndividualCustomerRequest.getIdentityNumber());
		
		IndividualCustomer individualCustomer = this.modelMapperService.forRequest()
				.map(updateIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerRepository.save(individualCustomer);
		return new SuccessResult("INDIVIDUAL.CUSTOMER.UPDATED");
	}

	@Override
	public DataResult<List<GetAllIndividualCustomersResponse>> getAll() {
		List<IndividualCustomer> individualCustomers = this.individualCustomerRepository.findAll();
		List<GetAllIndividualCustomersResponse> response = individualCustomers.stream()
				.map(user -> this.modelMapperService.forResponse().map(user, GetAllIndividualCustomersResponse.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllIndividualCustomersResponse>>(response);
	}

	@Override
	public DataResult<GetIndividualCustomerResponse> getById(int id) {
		checkIfIndividualCustomerIdExists(id);

		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByIndividualCustomerId(id);
		GetIndividualCustomerResponse response = this.modelMapperService.forResponse().map(individualCustomer,
				GetIndividualCustomerResponse.class);
		return new SuccessDataResult<GetIndividualCustomerResponse>(response);
	}
	
	@Override
	public DataResult<List<GetAllIndividualCustomersResponse>> getAll(Integer pageNo, Integer pageSize) {
		Pageable pageable =  PageRequest.of(pageNo-1,pageSize);
		
		List<IndividualCustomer> individualCustomers = this.individualCustomerRepository.findAll(pageable).getContent();
		List<GetAllIndividualCustomersResponse> response = individualCustomers.stream()
				.map(user -> this.modelMapperService.forResponse().map(user, GetAllIndividualCustomersResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllIndividualCustomersResponse>>(response);
	}

	private void checkIfRealPerson(CreateIndividualCustomerRequest createIndividualCustomerRequest)
			throws NumberFormatException, RemoteException {
		if (!userValidationService.checkIfRealPerson(createIndividualCustomerRequest)) {
			throw new BusinessException("USER.VALIDATION.ERROR");
		}
	}

	private void checkIfRealPerson(UpdateIndividualCustomerRequest updateIndividualCustomerRequest)
			throws NumberFormatException, RemoteException {
		if (!userValidationService.checkIfRealPerson(updateIndividualCustomerRequest)) {
			throw new BusinessException("USER.VALIDATION.ERROR");
		}
	}

	private void checkIfEmailIsSame(String email) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByEmail(email);
		if (individualCustomer != null) {
			throw new BusinessException("EMAIL.EXIST");
		}
	}

	private void checkIfIdentityIsSame(String identity) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByIdentityNumber(identity);
		if (individualCustomer != null) {
			throw new BusinessException("USER.EXIST");
		}
	}

	private void checkIfIndividualCustomerIdExists(int id) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository.findByIndividualCustomerId(id);
		if (individualCustomer == null) {
			throw new BusinessException("THERE.IS.NO.CUSTOMER");
		}
	}

	private void checkIfEmailIsSameForUpdate(int individualId, String email) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository
				.findByIndividualCustomerId(individualId);
		if (!individualCustomer.getEmail().equals(email)) {
			checkIfEmailIsSame(email);
		}
	}

	private void checkIfIdentityIsSameForUpdate(int individualId, String identiy) {
		IndividualCustomer individualCustomer = this.individualCustomerRepository
				.findByIndividualCustomerId(individualId);
		if (!individualCustomer.getIdentityNumber().equals(identiy)) {
			checkIfIdentityIsSame(identiy);
		}
	}

}
