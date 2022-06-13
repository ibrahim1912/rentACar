package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalFeatureItemService;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.CreateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.DeleteAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.UpdateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAdditionalFeatureItemResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAllAdditionalFeatureItemsResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureItemRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureItem;

@Service
public class AdditionalFeatureItemManager implements AdditionalFeatureItemService {

	private AdditionalFeatureItemRepository additionalFeatureItemRepository;
	private ModelMapperService modelMapperService;

	@Autowired
	public AdditionalFeatureItemManager(AdditionalFeatureItemRepository additionalFeatureItemRepository,
			ModelMapperService modelMapperService) {

		this.additionalFeatureItemRepository = additionalFeatureItemRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateAdditionalFeatureItemRequest createAdditionalFeatureItemRequest) {
		AdditionalFeatureItem additionalFeatureItem = this.modelMapperService.forRequest()
				.map(createAdditionalFeatureItemRequest, AdditionalFeatureItem.class);

		this.additionalFeatureItemRepository.save(additionalFeatureItem);
		return new SuccessResult("ITEM.ADDED");
	}

	@Override
	public Result delete(DeleteAdditionalFeatureItemRequest deleteAdditionalFeatureItemRequest) {
		this.additionalFeatureItemRepository.deleteById(deleteAdditionalFeatureItemRequest.getId());
		return new SuccessResult("ITEM.DELETED");
	}

	@Override
	public Result update(UpdateAdditionalFeatureItemRequest updateAdditionalFeatureItemRequest) {
		AdditionalFeatureItem additionalFeatureItem = this.modelMapperService.forRequest()
				.map(updateAdditionalFeatureItemRequest, AdditionalFeatureItem.class);
		this.additionalFeatureItemRepository.save(additionalFeatureItem);
		return new SuccessResult("ITEM.UPDATED");
	}

	@Override
	public DataResult<List<GetAllAdditionalFeatureItemsResponse>> getAll() {
		List<AdditionalFeatureItem> additionalFeatureItems = this.additionalFeatureItemRepository.findAll();
		List<GetAllAdditionalFeatureItemsResponse> response = additionalFeatureItems.stream().map(
				item -> this.modelMapperService.forResponse().map(item, GetAllAdditionalFeatureItemsResponse.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<GetAllAdditionalFeatureItemsResponse>>(response,"ITEM.LISTED");
	}

	@Override
	public DataResult<GetAdditionalFeatureItemResponse> getById(int id) {
		AdditionalFeatureItem additionalFeatureItem = this.additionalFeatureItemRepository.findById(id);
		GetAdditionalFeatureItemResponse  response = this.modelMapperService.forResponse().map(additionalFeatureItem, GetAdditionalFeatureItemResponse.class);
		return new SuccessDataResult<GetAdditionalFeatureItemResponse>(response);
	}

}
