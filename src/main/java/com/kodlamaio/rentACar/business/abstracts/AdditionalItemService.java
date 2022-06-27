package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.additionalItems.CreateAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalItems.DeleteAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.additionalItems.UpdateAdditionalItemRequest;
import com.kodlamaio.rentACar.business.responses.additionalItems.GetAdditionalItemResponse;
import com.kodlamaio.rentACar.business.responses.additionalItems.GetAllAdditionalItemsResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface AdditionalItemService {
	
	Result add(CreateAdditionalItemRequest createAdditionalItemRequest);
	Result delete (DeleteAdditionalItemRequest deleteAdditionalItemRequest);
	Result update (UpdateAdditionalItemRequest updateAdditionalItemRequest);
	DataResult<List<GetAllAdditionalItemsResponse>> getAll();
	DataResult<GetAdditionalItemResponse> getById(int id);
}
