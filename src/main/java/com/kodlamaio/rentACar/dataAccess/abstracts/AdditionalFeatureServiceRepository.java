package com.kodlamaio.rentACar.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;

public interface AdditionalFeatureServiceRepository extends JpaRepository<AdditionalFeatureService,Integer> { 
	AdditionalFeatureService findById(int id);
	List<AdditionalFeatureService> getByRentalId(int rentalId);
	
	
}
