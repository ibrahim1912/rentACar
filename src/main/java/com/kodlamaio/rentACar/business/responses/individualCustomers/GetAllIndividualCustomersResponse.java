package com.kodlamaio.rentACar.business.responses.individualCustomers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllIndividualCustomersResponse {

	private int individualCustomerId;
	private String email;
	private String password;
	private String identityNumber;
	private String firstName;
	private String lastName;
	private int birthDate;
}
