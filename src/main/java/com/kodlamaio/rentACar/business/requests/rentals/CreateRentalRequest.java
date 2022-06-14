package com.kodlamaio.rentACar.business.requests.rentals;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequest {

	//private int id;
	private int carId;
	private LocalDate pickUpDate;
	private LocalDate returnDate;
	private int pickUpCityId;
	private int returnCityId;

	
}
