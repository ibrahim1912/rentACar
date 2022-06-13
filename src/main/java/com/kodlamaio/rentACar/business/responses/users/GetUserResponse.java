package com.kodlamaio.rentACar.business.responses.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

	private int id;
	private String identityNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
}
