package com.kodlamaio.rentACar.business.requests.maintenances;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequest {

	private int id;
	private Date datesent;
	private Date datereturned;
	private int  carId;

	
}
