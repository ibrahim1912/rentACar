package com.kodlamaio.rentACar.business.requests.corporatecustomers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCorporateCustomerRequest {

	private int corporateCustomerId;
	
	@NotBlank
	@NotNull
	@Pattern(regexp = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@" 
        + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$", message="Write according to the rules name@domain.com")
	/**Email rules:
	*"name@domain.com"*/
	private String email;
	
	@NotBlank
	@NotNull
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$", message = "Conditions do not meet")
	/**Password rules :
	*Min 1 uppercase letter.
	*Min 1 lowercase letter.
	*Min 1 special character.
	*Min 1 number.
	*Min 8 characters.**/
	private String password;
	
	@NotBlank
	@NotNull
	@Size(min=3,max=50,message="Must be at least three characters.")
	private String companyName;
	
	@NotBlank
	@NotNull
	@Pattern(regexp = "[0-9]{11}", message = "Length must be 11")
	private String taxNumber;
}
