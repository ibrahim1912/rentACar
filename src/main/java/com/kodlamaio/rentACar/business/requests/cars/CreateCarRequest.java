package com.kodlamaio.rentACar.business.requests.cars;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
	
	private String description;
	private double dailyPrice;
	private int brandId;
	private int colorId;
	private String plate;
	private int kilometer;
}
