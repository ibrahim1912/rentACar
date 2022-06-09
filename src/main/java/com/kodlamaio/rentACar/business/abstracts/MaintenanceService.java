package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.maintenances.CreateMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.DeleteMaintenanceRequest;
import com.kodlamaio.rentACar.business.requests.maintenances.UpdateMaintenanceRequest;
import com.kodlamaio.rentACar.business.responses.maintenances.ReadMaintenanceResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.Maintenance;

public interface MaintenanceService {

	Result add(CreateMaintenanceRequest createMaintenanceRequest );
	Result update(UpdateMaintenanceRequest updateMaintenanceRequest);
	Result updateState(UpdateMaintenanceRequest updateMaintenanceRequest);
	Result delete (DeleteMaintenanceRequest deleteMaintenanceRequest);
	DataResult<List<Maintenance>> getAll();
	DataResult<Maintenance> getById(ReadMaintenanceResponse readMaintenanceResponce);
}
