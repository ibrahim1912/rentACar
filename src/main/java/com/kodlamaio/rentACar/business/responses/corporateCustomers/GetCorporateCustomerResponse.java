package com.kodlamaio.rentACar.business.responses.corporateCustomers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCorporateCustomerResponse {

	private int corporateCustomerId;
	private String email;
	private String password;
	private String companyName;
	private String taxNumber;
}
