package com.kodlamaio.rentACar.business.requests.additionalItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdditionalItemRequest {

	private int id;
	private String name;
	private double price;
}
