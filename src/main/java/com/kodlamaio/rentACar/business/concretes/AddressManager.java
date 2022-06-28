package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
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
import com.kodlamaio.rentACar.dataAccess.abstracts.CustomerRepository;
import com.kodlamaio.rentACar.entities.concretes.Address;
import com.kodlamaio.rentACar.entities.concretes.Customer;

@Service
public class AddressManager implements AddressService {
	
	private AddressRepository addressRepository;
	private ModelMapperService modelMapperService;
	private CustomerRepository customerRepository;
	
	@Autowired
	public AddressManager(AddressRepository addressRepository, ModelMapperService modelMapperService,
			CustomerRepository customerRepository) {
	
		this.addressRepository = addressRepository;
		this.modelMapperService = modelMapperService;
		this.customerRepository = customerRepository;
	}

	@Override
	public Result add(CreateAddressRequest createAddressRequest) {
		checkIfCustomerIdExists(createAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		address.setInvoiceAddress(createAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED");
	}
	
	
	@Override  //fatura adresi farklıysa ekler
	public Result addInvoiceAddressIfDifferent(CreateAddressRequest createAddressRequest) {
		checkIfCustomerIdExists(createAddressRequest.getCustomerId());
		
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
		checkIfCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	
	
	@Override //iletişim adresi güncellenince ikiside güncelleniyor
	public Result updateIfBothAreSame(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		address.setInvoiceAddress(updateAddressRequest.getContactAddress());
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
	
	
	private void checkIfCustomerIdExists(int id) {
		Customer customer = this.customerRepository.findById(id);
		if(customer == null) {
			throw new BusinessException("CUSTOMER.NOT.EXISTS");
		}
	}
	

}
