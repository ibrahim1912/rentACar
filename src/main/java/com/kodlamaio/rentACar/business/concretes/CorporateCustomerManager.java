package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.CorporateCustomerService;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.CreateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.DeleteCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.requests.corporateCustomers.UpdateCorporateCustomerRequest;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetAllCorporateCustomersResponse;
import com.kodlamaio.rentACar.business.responses.corporateCustomers.GetCorporateCustomerResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.CorporateCustomerRepository;
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;


@Service
public class CorporateCustomerManager implements CorporateCustomerService {

	private CorporateCustomerRepository corporateCustomerRepository;
	private ModelMapperService modelMapperService;

	@Autowired
	public CorporateCustomerManager(CorporateCustomerRepository corporateCustomerRepository,
			ModelMapperService modelMapperService) {

		this.corporateCustomerRepository = corporateCustomerRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {
		checkIfCompanyNameIsSame(createCorporateCustomerRequest.getCompanyName());
		
		CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(createCorporateCustomerRequest,
				CorporateCustomer.class);
		this.corporateCustomerRepository.save(corporateCustomer);
		return new SuccessResult("CORPORATE.CUSTOMER.ADDED");
	}

	@Override
	public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {
		checkIfCorporateCustomerIdExists(updateCorporateCustomerRequest.getCorporateCustomerId());
		
		CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(updateCorporateCustomerRequest, CorporateCustomer.class);
		this.corporateCustomerRepository.save(corporateCustomer);
		return new SuccessResult("CORPORATE.CUSTOMER.UPDATED");
	}

	@Override
	public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {
		checkIfCorporateCustomerIdExists(deleteCorporateCustomerRequest.getCorporateCustomerId());
		
		this.corporateCustomerRepository.deleteById(deleteCorporateCustomerRequest.getCorporateCustomerId());
		return new SuccessResult("CORPORATE.CUSTOMER.DELETED");
	}

	@Override
	public DataResult<List<GetAllCorporateCustomersResponse>> getAll() {
		List<CorporateCustomer> customers = this.corporateCustomerRepository.findAll();
		List<GetAllCorporateCustomersResponse> response = customers.stream().map(
				customer -> this.modelMapperService.forResponse().map(customer, GetAllCorporateCustomersResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllCorporateCustomersResponse>>(response);
	}

	@Override
	public DataResult<GetCorporateCustomerResponse> getById(int id) {
		checkIfCorporateCustomerIdExists(id);
		
		CorporateCustomer corporateCustomer = this.corporateCustomerRepository.findByCorporateCustomerId(id);
		GetCorporateCustomerResponse response = this.modelMapperService.forResponse().map(corporateCustomer, GetCorporateCustomerResponse.class);
		return new SuccessDataResult<GetCorporateCustomerResponse>(response);
	}
	
	@Override
	public CorporateCustomer getByCorporateCustomerId(int corporateCustomerId) {
		checkIfCorporateCustomerIdExists(corporateCustomerId);
		
		CorporateCustomer corporateCustomer = this.corporateCustomerRepository.findByCorporateCustomerId(corporateCustomerId);
		return corporateCustomer;
	}

	private void checkIfCorporateCustomerIdExists(int id) {
		CorporateCustomer corporateCustomer = this.corporateCustomerRepository.findByCorporateCustomerId(id);
		if(corporateCustomer == null) {
			throw new BusinessException("THERE.IS.NOT.A.CORPORATE.CUSTOMER");
		}
	}
	
	private void checkIfCompanyNameIsSame(String companyName) {
		CorporateCustomer corporateCustomer = this.corporateCustomerRepository.findByCompanyName(companyName);
		if(corporateCustomer != null){
			throw new BusinessException("COMPANY.NAME.EXISTS");
		}
	}

	

}
