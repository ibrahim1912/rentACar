package com.kodlamaio.rentACar.business.requests.cars;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarRequest {
	
	private int id;
	@NotEmpty
	@NotBlank
	@Size(min=3,max=20)
	private String description;
	@Min(10)
	private double dailyPrice;
	private int cityId;
	private int brandId;
	private int colorId; 
	private int kilometer;
	private int minFindeksScore;
}
