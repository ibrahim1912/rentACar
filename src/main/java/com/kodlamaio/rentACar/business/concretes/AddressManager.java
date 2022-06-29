package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AddressService;
import com.kodlamaio.rentACar.business.abstracts.CorporateCustomerService;
import com.kodlamaio.rentACar.business.abstracts.IndividualCustomerService;
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
import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;
import com.kodlamaio.rentACar.entities.concretes.Customer;
import com.kodlamaio.rentACar.entities.concretes.IndividualCustomer;

@Service
public class AddressManager implements AddressService {

	private AddressRepository addressRepository;
	private ModelMapperService modelMapperService;
	private CustomerRepository customerRepository;
	private IndividualCustomerService individualCustomerService;
	private CorporateCustomerService corporateCustomerService;

	@Autowired
	public AddressManager(AddressRepository addressRepository, ModelMapperService modelMapperService,
			CustomerRepository customerRepository, IndividualCustomerService individualCustomerService,
			CorporateCustomerService corporateCustomerService) {

		this.addressRepository = addressRepository;
		this.modelMapperService = modelMapperService;
		this.customerRepository = customerRepository;
		this.individualCustomerService = individualCustomerService;
		this.corporateCustomerService = corporateCustomerService;
	}


	@Override
	public Result addForIndividualCustomer(CreateAddressRequest createAddressRequest) {
		checkIfIndividualCustomerIdExists(createAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		address.setInvoiceAddress(createAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED.FOR.INDIVIDUAL.CUSTOMER");
	}

	@Override
	public Result addForCorporateCustomer(CreateAddressRequest createAddressRequest) {
		checkIfCorporateCustomerIdExists(createAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		address.setInvoiceAddress(createAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED.FOR.CORPORATE.CUSTOMER");
	}

	@Override // fatura adresi farklıysa ekler
	public Result addInvoiceAddressIfDifferentForIndividualCustomer(CreateAddressRequest createAddressRequest) {
		checkIfIndividualCustomerIdExists(createAddressRequest.getCustomerId());

		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED.FOR.INDIVIDUAL.CUSTOMER");
	}
	
	@Override // fatura adresi farklıysa ekler
	public Result addInvoiceAddressIfDifferentForCorporateCustomer(CreateAddressRequest createAddressRequest) {
		checkIfCorporateCustomerIdExists(createAddressRequest.getCustomerId());

		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED.FOR.CORPORATE.CUSTOMER");
	}

	@Override
	public Result delete(DeleteAddressRequest deleteAddressRequest) {
		checkIfAddressIdExists(deleteAddressRequest.getId());

		this.addressRepository.deleteById(deleteAddressRequest.getId());
		return new SuccessResult("ADDRESS.DELETED");
	}

	@Override // her ikisini de güncellenir ayrı ayrı yazmak gerekiyor
	public Result updateForIndividualCustomer(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfIndividualCustomerIdExists(updateAddressRequest.getCustomerId());

		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED.FOR.INDIVIDUAL.CUSTOMER");
	}
	
	@Override // her ikisini de güncellenir ayrı ayrı yazmak gerekiyor
	public Result updateForCorporateCustomer(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());

		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED.FOR.CORPORATE.CUSTOMER");
	}
	
	@Override // iletişim adresi güncellenince ikiside güncelleniyor
	public Result updateIfBothAreSameForIndividualCustomer(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		address.setInvoiceAddress(updateAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED.FOR.INDIVIDUAL.CUSTOMER");
	}


	@Override // iletişim adresi güncellenince ikiside güncelleniyor
	public Result updateIfBothAreSameForCorporateCustomer(UpdateAddressRequest updateAddressRequest) {
		checkIfAddressIdExists(updateAddressRequest.getId());
		checkIfCorporateCustomerIdExists(updateAddressRequest.getCustomerId());
		
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		address.setInvoiceAddress(updateAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED.FOR.CORPORATE.CUSTOMER");
	}

	@Override
	public DataResult<List<GetAllAddressesResponse>> getAll() {
		List<Address> addresses = this.addressRepository.findAll();
		List<GetAllAddressesResponse> response = addresses.stream()
				.map(address -> this.modelMapperService.forResponse().map(address, GetAllAddressesResponse.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllAddressesResponse>>(response, "ADDRESSES.LISTED");
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
		if (address == null) {
			throw new BusinessException("THERE.IS.NOT.ADDRESS");
		}
	}


	private void checkIfIndividualCustomerIdExists(int individualCustomerId) {
		IndividualCustomer individualCustomer = this.individualCustomerService
				.getByIndividualCustomerId(individualCustomerId);
		if (individualCustomer == null) {
			throw new BusinessException("INDIVIDUAL.CUSTOMER.NOT.EXISTS");
		}
	}

	private void checkIfCorporateCustomerIdExists(int corporateCustomerId) {
		CorporateCustomer corporateCustomer = this.corporateCustomerService
				.getByCorporateCustomerId(corporateCustomerId);
		if (corporateCustomer == null) {
			throw new BusinessException("CORPORATE.CUSTOMER.NOT.EXISTS");
		}
	}


	
}
