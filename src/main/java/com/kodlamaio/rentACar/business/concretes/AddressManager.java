package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AddressService;
import com.kodlamaio.rentACar.business.requests.addresses.CreateAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.DeleteAddressRequest;
import com.kodlamaio.rentACar.business.requests.addresses.UpdateAddressRequest;
import com.kodlamaio.rentACar.business.responses.addresses.GetAddressResponse;
import com.kodlamaio.rentACar.business.responses.addresses.GetAllAddressesResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AddressRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.CorporateCustomerRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.IndividualCustomerRepository;
import com.kodlamaio.rentACar.entities.concretes.Address;
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;

@Service
public class AddressManager implements AddressService {
	
	private AddressRepository addressRepository;
	private ModelMapperService modelMapperService;
	private IndividualCustomerRepository individualCustomerRepository;
	private CorporateCustomerRepository corporateCustomerRepository;

	
	public AddressManager(AddressRepository addressRepository, ModelMapperService modelMapperService,
			IndividualCustomerRepository individualCustomerRepository,
			CorporateCustomerRepository corporateCustomerRepository) {
	
		this.addressRepository = addressRepository;
		this.modelMapperService = modelMapperService;
		this.individualCustomerRepository = individualCustomerRepository;
		this.corporateCustomerRepository = corporateCustomerRepository;
	}


	

	@Override
	public Result add(CreateAddressRequest createAddressRequest) {
		checkIfCorporateCustomerIdExists(createAddressRequest.getCustomerId());
		checkIfIndividualCustomerIdExists(createAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		address.setInvoiceAddress(createAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED");
	}
	
	@Override  //fatura adresi farklıysa ekler
	public Result addInvoiceAddressIfDifferent(CreateAddressRequest createAddressRequest) {
		checkIfCorporateCustomerIdExists(createAddressRequest.getCustomerId());
		checkIfIndividualCustomerIdExists(createAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED");
	}

	@Override
	public Result delete(DeleteAddressRequest deleteAddressRequest) {
		checkIfAddressIdExists(deleteAddressRequest.getId());
		
		this.addressRepository.deleteById(deleteAddressRequest.getId());
		return new SuccessResult("ADDRESS.DELETED");
	}
	
	@Override //her ikisini de güncellenir ayrı ayrı yazmak gerekiyor
	public Result update(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfIndividualCustomerIdExists(updateAddressRequest.getCustomerId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	
	
	@Override //ileteşim adresi güncellenince ikiside güncelleniyor
	public Result updateToSame(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfIndividualCustomerIdExists(updateAddressRequest.getCustomerId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		address.setInvoiceAddress(updateAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}

	@Override // sadece iletişim adresi güncellenir Fatura aynı kalır değişmez
	public Result updateToContactAddress(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfIndividualCustomerIdExists(updateAddressRequest.getCustomerId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		Address addressFromRespository = this.addressRepository.findById(updateAddressRequest.getId());
		address.setInvoiceAddress(addressFromRespository.getInvoiceAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	
	@Override // sadece fatura adresi güncellenir 
	public Result updateToInvoiceAddress(UpdateAddressRequest updateAddressRequest) {
		
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfIndividualCustomerIdExists(updateAddressRequest.getCustomerId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		Address addressFromRespository = this.addressRepository.findById(updateAddressRequest.getId());
		address.setContactAddress(addressFromRespository.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	

	@Override
	public DataResult<List<GetAllAddressesResponse>> getAll() {
		List<Address> addresses = this.addressRepository.findAll();
		List<GetAllAddressesResponse> response = addresses.stream()
				.map(address -> this.modelMapperService.forResponse().map(address, GetAllAddressesResponse.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllAddressesResponse>>(response,"ADDRESSES.LISTED");
	}

	@Override
	public DataResult<GetAddressResponse> getById(int id) {
		checkIfAddressIdExists(id);
		
		Address address = this.addressRepository.findById(id);
		GetAddressResponse response = this.modelMapperService.forResponse().map(address, GetAddressResponse.class);
		return new SuccessDataResult<GetAddressResponse>(response);
	}
	
	private void checkIfAddressIdExists(int id) {
		Address address = this.addressRepository.findById(id);
		if(address == null) {
			throw new BusinessException("THERE.IS.NOT.ADDRESS");
		}
	}
	
	private void checkIfIndividualCustomerIdExists(int customerId) {
		IndividualCustomer customer = this.individualCustomerRepository.findByIndividualCustomerId(customerId);
		if(customer == null) {
			throw new BusinessException("THERE.IS.NOT.A.INDIVIDUAL.CUSTOMER");
		}
	}
	
	private void checkIfCorporateCustomerIdExists(int customerId) {
		CorporateCustomer customer = this.corporateCustomerRepository.findByCorporateCustomerId(customerId);
		if(customer == null) {
			throw new BusinessException("THERE.IS.NOT.A.CORPORATE.CUSTOMER");
		}
	}
	

}
