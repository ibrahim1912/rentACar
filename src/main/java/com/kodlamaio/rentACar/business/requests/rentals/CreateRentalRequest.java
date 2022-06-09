package com.kodlamaio.rentACar.business.requests.rentals;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequest {

	private int id;
	private int carId;
	private Date pickUpDate;
	private Date returnDate;
	
}
