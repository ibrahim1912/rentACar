package com.kodlamaio.rentACar;




import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.results.ErrorDataResult;

@SpringBootApplication
@RestControllerAdvice // Aspect

public class RentACarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentACarApplication.class, args);
	}

	@Bean //belleğe attık
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@ExceptionHandler  //git bak methodArgumentNotValidException ne döndürüyor
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // return olarak bad request döndür
	public ErrorDataResult<Object> handleValidationExceptions(MethodArgumentNotValidException  methodArgumentNotValidException) {
		Map<String ,String> validationErrors = new HashMap<String, String>();
		for(FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
			validationErrors.put(fieldError.getField(),fieldError.getDefaultMessage());
		}
		
		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<Object>(validationErrors,"VALIDATION_ERROR(S)");
		return errorDataResult;
	}
	
	
	
	@ExceptionHandler  //git bak businessException ne döndürüyor
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // return olarak bad request döndür
	public ErrorDataResult<Object> handleBusinessExceptions(BusinessException businessException) {
		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<Object>(businessException.getMessage(),
				"BUSINESS.EXCEPTION");
		return errorDataResult;
	}
	
	
	

}
