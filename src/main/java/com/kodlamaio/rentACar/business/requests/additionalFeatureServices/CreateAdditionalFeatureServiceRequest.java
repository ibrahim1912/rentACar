package com.kodlamaio.rentACar.business.requests.additionalFeatureServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdditionalFeatureServiceRequest {
	private int id;
	private int rentalId;
	private int additionalFeatureItemId;
}
