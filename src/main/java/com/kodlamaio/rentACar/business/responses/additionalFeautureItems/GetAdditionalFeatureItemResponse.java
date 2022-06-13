package com.kodlamaio.rentACar.business.responses.additionalFeautureItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdditionalFeatureItemResponse {

	private int id;
	private String name;
	private double price;
}
