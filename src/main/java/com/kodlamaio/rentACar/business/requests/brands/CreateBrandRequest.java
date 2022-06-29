package com.kodlamaio.rentACar.business.requests.brands;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateBrandRequest { 
	
	@NotEmpty
	@NotBlank
	@Size(min=3,max=20)
	private String name;
}
