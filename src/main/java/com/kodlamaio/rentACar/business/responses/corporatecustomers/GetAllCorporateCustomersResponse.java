package com.kodlamaio.rentACar.business.responses.corporatecustomers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCorporateCustomersResponse {

	
	private int corporateCustomerId;
	private String email;
	private String password;
	private String companyName;
	private String taxNumber;
}
