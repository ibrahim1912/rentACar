package com.kodlamaio.rentACar.core.utilities.aspects;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Log {

	private LocalDate date;
	private String classsName;
	private String methodName;
	
	private List<String> parameterNames;
}
