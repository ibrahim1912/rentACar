package com.kodlamaio.rentACar.business.responses.additionalFeatureServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllAdditionalFeatureServicesResponse  {

	private int id;
	private int rentalId;
	private String additionalFeatureItemName;
	private int additionalFeatureItemId;
	private double additionalFeatureItemPrice;
	private double totalPrice;
	private int totalDay;
	private String userFirstName;
}
