package com.kodlamaio.rentACar.business.requests.addresses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateAddressRequest {
	
	private String contactAddress;
	private String invoiceAddress;
	private int customerId;
	

}
