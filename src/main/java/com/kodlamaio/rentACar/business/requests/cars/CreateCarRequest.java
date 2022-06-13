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
public class CreateCarRequest {
	
	//single responsibilty oldugu için buraya yazarız
	@NotEmpty
	@NotBlank
	@Size(min=3,max=20)
	private String description;
	
	@Min(10)
	private double dailyPrice;
	
	private int brandId;
	private int colorId;
	private String plate;
	private int kilometer;
	private int cityId;
	
}
