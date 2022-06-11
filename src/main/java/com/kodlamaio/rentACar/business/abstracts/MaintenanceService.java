package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.maintenances.CreateMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.DeleteMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.UpdateMaintenanceRequest;
import com.kodlamaio.rentACar.business.responses.maintenances.GetAllMaintenancesResponse;
import com.kodlamaio.rentACar.business.responses.maintenances.GetMaintenanceResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

public interface MaintenanceService {

	Result add(CreateMaintenanceRequest createMaintenanceRequest );
	Result update(UpdateMaintenanceRequest updateMaintenanceRequest);
	Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest);
	Result delete (DeleteMaintenanceRequest deleteMaintenanceRequest);
	DataResult<List<GetAllMaintenancesResponse>> getAll();
	DataResult<GetMaintenanceResponse> getById(int id);
	
}
