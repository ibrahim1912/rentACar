package com.kodlamaio.rentACar.business.responses.rentals;


import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRentalsResponse {

	private int id;
	private LocalDate pickUpDate;
	private LocalDate returnDate;
	private int totalDays;
	private double totalPrice;
	private String brandName;
	private String colorName;
	private int carId;
	private String cityName;
	private int pickUpCityId;
	private int returnCityId;
	private int customerId;
	private String email;
	private String customer;
	private String identiyNumber;
	


}
