package com.kodlamaio.rentACar.business.requests.maintenances;


import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequest {

	private int id;
	private LocalDate sentDate;
	private LocalDate returnedDate;
	private int carId;

	
}
