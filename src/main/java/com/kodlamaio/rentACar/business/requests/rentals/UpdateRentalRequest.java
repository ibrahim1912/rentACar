package com.kodlamaio.rentACar.business.requests.rentals;


import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentalRequest {

	private int id;
	private int carId;
	private LocalDate returnDate;
	private int returnCityId;
}
