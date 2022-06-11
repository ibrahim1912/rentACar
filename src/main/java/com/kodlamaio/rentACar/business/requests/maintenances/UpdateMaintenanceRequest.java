package com.kodlamaio.rentACar.business.requests.maintenances;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaintenanceRequest {

	private int id;
	private Date datereturned;
	//private Date datesent;
	private int carId;
}
