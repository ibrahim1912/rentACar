package com.kodlamaio.rentACar.business.responses.rentals;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRentalsResponse {

	private int id;
	private Date pickUpDate;
	private Date returnDate;
	private int totalDays;
	private double totalPrice;
	private String brandName;
	private String colorName;
	private int carId;

}
