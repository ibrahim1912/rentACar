package com.kodlamaio.rentACar.business.concretes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureServiceService;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.CreateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.DeleteAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.UpdateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAdditionalFeatureServiceResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAllAdditionalFeatureServicesResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureServiceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureItem;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class AdditionalFeatureServiceManager implements AdditionalFeatureServiceService {

	private AdditionalFeatureServiceRepository additionalFeatureServiceRepository;
	private ModelMapperService modelMapperService;
	private AdditionalFeatureItemRepository additionalFeatureItemRepository;
	private RentalRepository rentalRepository;

	@Autowired
	public AdditionalFeatureServiceManager(AdditionalFeatureServiceRepository additionalFeatureServiceRepository,
			ModelMapperService modelMapperService, AdditionalFeatureItemRepository additionalFeatureItemRepository,
			RentalRepository rentalRepository) {

		this.additionalFeatureServiceRepository = additionalFeatureServiceRepository;
		this.modelMapperService = modelMapperService;
		this.additionalFeatureItemRepository = additionalFeatureItemRepository;
		this.rentalRepository = rentalRepository;
	}

	@Override
	public Result add(CreateAdditionalFeatureServiceRequest createAdditionalFeatureService) {
		AdditionalFeatureService additionalFeatureService = this.modelMapperService.forRequest()
				.map(createAdditionalFeatureService, AdditionalFeatureService.class);
		Rental rental = this.rentalRepository.findById(additionalFeatureService.getRental().getId());
		AdditionalFeatureItem item = this.additionalFeatureItemRepository
				.findById(additionalFeatureService.getAdditionalFeatureItem().getId());
		double price = item.getPrice();
		int totalDays = rental.getTotalDays();
		double totalPrice = totalDays * price;

		additionalFeatureService.setTotalPrice(totalPrice);
		additionalFeatureService.setTotalDay(totalDays);
		
		//rental.setTotalPrice(totalPrice + rental.getTotalPrice());
		this.additionalFeatureServiceRepository.save(additionalFeatureService);
		return new SuccessResult("ADDITIONAL.SERVICE.ADDED");
	}

	@Override
	public Result update(UpdateAdditionalFeatureServiceRequest updateAdditionalFeatureServiceRequest) {
		AdditionalFeatureService additionalFeatureService = this.modelMapperService.forRequest()
				.map(updateAdditionalFeatureServiceRequest, AdditionalFeatureService.class);
		Rental rental = this.rentalRepository.findById(additionalFeatureService.getRental().getId());
		AdditionalFeatureItem item = this.additionalFeatureItemRepository
				.findById(additionalFeatureService.getAdditionalFeatureItem().getId());
		double price = item.getPrice();
		int totalDays = rental.getTotalDays();
		double totalPrice = totalDays * price;
		additionalFeatureService.setTotalPrice(totalPrice);
		additionalFeatureService.setTotalDay(totalDays);
		this.additionalFeatureServiceRepository.save(additionalFeatureService);
		return new SuccessResult("SERVICES.UPDATE");
	}

	@Override
	public Result delete(DeleteAdditionalFeatureServiceRequest deleteAdditionalFeatureServiceRequest) {
		this.additionalFeatureServiceRepository.deleteById(deleteAdditionalFeatureServiceRequest.getId());
		return new SuccessResult("SERVICE.DELETED");
	}

	@Override
	public DataResult<List<GetAllAdditionalFeatureServicesResponse>> getAll() {
		List<AdditionalFeatureService> additionalFeatureServices = this.additionalFeatureServiceRepository.findAll();
		List<GetAllAdditionalFeatureServicesResponse> response = additionalFeatureServices.stream()
				.map(service -> this.modelMapperService.forResponse().map(service,
						GetAllAdditionalFeatureServicesResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllAdditionalFeatureServicesResponse>>(response, "SERVICES.LISTED");
	}

	@Override
	public DataResult<GetAdditionalFeatureServiceResponse> getById(int id) {
		AdditionalFeatureService additionalFeatureService = this.additionalFeatureServiceRepository.findById(id);
		GetAdditionalFeatureServiceResponse response = this.modelMapperService.forResponse()
				.map(additionalFeatureService, GetAdditionalFeatureServiceResponse.class);
		return new SuccessDataResult<GetAdditionalFeatureServiceResponse>(response);
	}
	
	

}
