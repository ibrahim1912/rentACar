package com.kodlamaio.rentACar.core.utilities.exceptions;

public class BusinessException extends RuntimeException { //çalışma sırasında olan bir hata türü run time

	public BusinessException(String message) {
		super(message);
		
	}
}
