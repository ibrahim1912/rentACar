package com.kodlamaio.rentACar.business.responses.orderedAdditionalItems;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderedAdditionalItemResponse {
	private int id;
	private int rentalId;
	private String additionalItemName;
	private int additionaLIemId;
	private double additionalItemPrice;
	private LocalDate pickUpDate;
	private LocalDate returnDate;
	
}
