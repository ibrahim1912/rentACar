package com.kodlamaio.rentACar.business.responses.rentals;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRentalResponse {
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
	private String customer;
}
