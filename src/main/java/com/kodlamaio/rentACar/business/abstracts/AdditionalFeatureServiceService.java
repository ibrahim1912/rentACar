package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.CreateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.DeleteAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.requests.additionalFeatureServices.UpdateAdditionalFeatureServiceRequest;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAdditionalFeatureServiceResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeatureServices.GetAllAdditionalFeatureServicesResponse;
import com.kodlamaio.rentACar.business.responses.additionalFeautureItems.GetAllAdditionalFeatureItemsResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface AdditionalFeatureServiceService {
	
	Result add(CreateAdditionalFeatureServiceRequest createAdditionalFeatureService);
	Result update(UpdateAdditionalFeatureServiceRequest updateAdditionalFeatureServiceRequest);
	Result delete(DeleteAdditionalFeatureServiceRequest deleteAdditionalFeatureServiceRequest);
	
	DataResult<List<GetAllAdditionalFeatureServicesResponse>> getAll();
	DataResult<GetAdditionalFeatureServiceResponse> getById(int id);

	
}
