package com.kodlamaio.rentACar.business.responses.users;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {
	
	private String identityNumber;
	
	
	private String firstName;
	
	
	private String lastName;
	
	
	private String email;
	
	
	private String password;
}
