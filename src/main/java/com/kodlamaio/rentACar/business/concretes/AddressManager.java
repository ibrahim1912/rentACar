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
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AddressRepository;
import com.kodlamaio.rentACar.entities.concretes.Address;

@Service
public class AddressManager implements AddressService {
	
	private AddressRepository addressRepository;
	private ModelMapperService modelMapperService;

	@Autowired
	public AddressManager(AddressRepository addressRepository, ModelMapperService modelMapperService) {
		
		this.addressRepository = addressRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateAddressRequest createAddressRequest) {
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		address.setInvoiceAddress(createAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED");
	}
	
	@Override  //fatura adresi farklıysa ekler
	public Result addInvoiceAddressIfDifferent(CreateAddressRequest createAddressRequest) {
		Address address = this.modelMapperService.forRequest().map(createAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.ADDED");
	}

	@Override
	public Result delete(DeleteAddressRequest deleteAddressRequest) {
		this.addressRepository.deleteById(deleteAddressRequest.getId());
		return new SuccessResult("ADDRESS.DELETED");
	}
	
	@Override //her ikisini de güncellenir ayrı ayrı yazmak gerekiyor
	public Result update(UpdateAddressRequest updateAddressRequest) {
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	
	
	@Override //ileteşim adresi güncellenince ikiside güncelleniyor
	public Result updateToSame(UpdateAddressRequest updateAddressRequest) {
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		address.setInvoiceAddress(updateAddressRequest.getContactAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}

	@Override // sadece iletişim adresi güncellenir Fatura aynı kalır değişmez
	public Result updateToContactAddress(UpdateAddressRequest updateAddressRequest) {
		Address address = this.modelMapperService.forRequest().map(updateAddressRequest, Address.class);
		Address addressFromRespository = this.addressRepository.findById(updateAddressRequest.getId());
		address.setInvoiceAddress(addressFromRespository.getInvoiceAddress());
		this.addressRepository.save(address);
		return new SuccessResult("ADDRESS.UPDATED");
	}
	
	@Override // sadece fatura adresi güncellenir 
	public Result updateToInvoiceAddress(UpdateAddressRequest updateAddressRequest) {
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
		Address address = this.addressRepository.findById(id);
		GetAddressResponse response = this.modelMapperService.forResponse().map(address, GetAddressResponse.class);
		return new SuccessDataResult<GetAddressResponse>(response);
	}

	

}
