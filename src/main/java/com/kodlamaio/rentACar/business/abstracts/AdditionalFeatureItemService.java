package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.CreateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.DeleteAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureItems.UpdateAdditionalFeatureItemRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAdditionalFeatureItemResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAllAdditionalFeatureItemsResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface AdditionalFeatureItemService {
	
	Result add(CreateAdditionalFeatureItemRequest createAdditionalFeatureItemRequest);
	Result delete (DeleteAdditionalFeatureItemRequest deleteAdditionalFeatureItemRequest);
	Result update (UpdateAdditionalFeatureItemRequest updateAdditionalFeatureItemRequest);
	DataResult<List<GetAllAdditionalFeatureItemsResponse>> getAll();
	DataResult<GetAdditionalFeatureItemResponse> getById(int id);
}
