package com.kodlamaio.rentACar.business.requests.orderedAdditionalItems;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderedAdditionalItemRequest {
	private int rentalId;
	private int additionalItemId;
	private LocalDate pickUpDate;
	private LocalDate returnDate;
}
