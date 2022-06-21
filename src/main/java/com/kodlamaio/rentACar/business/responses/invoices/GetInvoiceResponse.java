package com.kodlamaio.rentACar.business.responses.invoices;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoiceResponse {

	private int id;
	private int rentalId;
	private String carKilometer;
	private double totalPrice;
	private String userFirstName;
	private LocalDate rentalPickUpDate;
	private LocalDate rentalReturnDate;
	private String brandName;
	private int status;
	private int invoiceNumber;
}
