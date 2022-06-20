package com.kodlamaio.rentACar.business.responses.cars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCarResponse {

	private int id;
	private String description;
	private String brandName;
	private String colorName;
	private int kilometer;
	private String state;
	private int cityId;
	private String cityName;
	private String plate;
	private double dailyPrice;

	
}
