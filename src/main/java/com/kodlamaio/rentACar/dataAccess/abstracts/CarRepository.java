package com.kodlamaio.rentACar.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
	
	Car findById(int id);
	List<Car> getByBrandId(int brandId);
	
	
}
