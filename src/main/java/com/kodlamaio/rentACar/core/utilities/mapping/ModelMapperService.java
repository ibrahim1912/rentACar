package com.kodlamaio.rentACar.core.utilities.mapping;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {

	ModelMapper forResponse();

	ModelMapper forRequest(); //Requestlerin map inglerini
}
