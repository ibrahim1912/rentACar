package com.kodlamaio.rentACar.business.responses.cities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCitiesResponse {
	
	private int id;
	private String name;

}
