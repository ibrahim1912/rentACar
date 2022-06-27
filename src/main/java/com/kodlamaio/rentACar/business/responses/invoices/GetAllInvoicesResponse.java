package com.kodlamaio.rentACar.business.responses.invoices;

import java.time.LocalDate;
import java.util.List;

import com.kodlamaio.rentACar.entities.concretes.AdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.OrderedAdditionalItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllInvoicesResponse {
	private int id;
	private int rentalId;
	private String carKilometer;
	private double totalPrice;
	private String userFirstName;
	private LocalDate rentalPickUpDate;
	private LocalDate rentalReturnDate;
	private String brandName;
	private int state;
	private int invoiceNumber;


}
