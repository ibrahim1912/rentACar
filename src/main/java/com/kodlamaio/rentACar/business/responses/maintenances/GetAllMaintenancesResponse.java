package com.kodlamaio.rentACar.business.responses.maintenances;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllMaintenancesResponse {

	private int id;
	private Date datesent;
	private Date datereturned;
	private int carId;
	private String description;
	private int state;
	
	
	
}
