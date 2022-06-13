package com.kodlamaio.rentACar.business.requests.users;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

	private int id;
	
	
	@NotBlank
	@NotNull
	private String email;
	
	@NotBlank
	@NotNull
	@Column(name="password")
	private String password;
}
