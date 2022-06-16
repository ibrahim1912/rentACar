package com.kodlamaio.rentACar.business.responses.cars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetAllCarsResponse {
	private int id;
	private double dailyPrice;
	private String description;
	private String brandName;
	private String colorName;
	private int kilometer;
	private int state;
	private String cityName;
	private int cityId;
	private int minFindeksScore;
	
}
