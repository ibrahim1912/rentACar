package com.kodlamaio.rentACar.business.requests.invoices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequest {
	private int invoiceNumber;
	private int rentalId;
}
