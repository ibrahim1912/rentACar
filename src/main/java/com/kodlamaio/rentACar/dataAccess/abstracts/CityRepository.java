package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;


import com.kodlamaio.rentACar.entities.concretes.City;

public interface CityRepository extends JpaRepository<City, Integer>{

	City findById(int id);
}
