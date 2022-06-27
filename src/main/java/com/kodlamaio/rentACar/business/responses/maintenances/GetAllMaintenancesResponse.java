package com.kodlamaio.rentACar.business.responses.maintenances;


import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllMaintenancesResponse {

	private int id;
	private LocalDate sentDate;
	private LocalDate returnedDate;
	private int carId;
	private String description;
	private int state;
	
	
	
}
