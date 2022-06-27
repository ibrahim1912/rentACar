package com.kodlamaio.rentACar.business.responses.addresses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAddressResponse {

	private int id;
	private String contactAddress;
	private String invoiceAddress;
	private String firstName;
}
