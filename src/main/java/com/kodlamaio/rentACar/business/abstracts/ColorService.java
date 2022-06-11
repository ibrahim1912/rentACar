package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.colors.CreateColorRequest;
import com.kodlamaio.rentACar.business.requests.colors.DeleteColorRequest;
import com.kodlamaio.rentACar.business.requests.colors.UpdateColorRequest;
import com.kodlamaio.rentACar.business.responses.colors.GetAllColorsResponse;
import com.kodlamaio.rentACar.business.responses.colors.GetColorResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface ColorService {
	
	Result add(CreateColorRequest createColorRequest);
	Result delete(DeleteColorRequest deleteColorRequest);
	Result update(UpdateColorRequest updateColorRequest);
	
	DataResult<List<GetAllColorsResponse>> getAll();
	DataResult<GetColorResponse> getById(int id);
	
}
