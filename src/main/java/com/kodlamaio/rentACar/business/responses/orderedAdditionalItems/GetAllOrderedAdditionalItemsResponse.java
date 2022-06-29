package com.kodlamaio.rentACar.business.responses.orderedAdditionalItems;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllOrderedAdditionalItemsResponse  {

	private int id;
	private int rentalId;
	private String additionalItemName;
	private int additionalItemId;
	private double additionalItemPrice;
	private double totalPrice;
	private int totalDay;
	private String userFirstName;
	private LocalDate pickUpDate;
	private LocalDate returnDate;
}
