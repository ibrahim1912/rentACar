package com.kodlamaio.rentACar.business.requests.additionalFeatureItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdditionalFeatureItemRequest {

	private int id;
	private String name;
	private double price;
	
}
