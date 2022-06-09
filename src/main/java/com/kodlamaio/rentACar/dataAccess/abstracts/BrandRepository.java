package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.Brand;
 
//elimizde crud versiyonları hazır
public interface BrandRepository extends JpaRepository<Brand,Integer> {
	
	Brand findById(int id);
	
	
}
