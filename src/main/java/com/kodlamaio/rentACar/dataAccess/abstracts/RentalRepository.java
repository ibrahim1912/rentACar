package com.kodlamaio.rentACar.dataAccess.abstracts;



import org.springframework.data.jpa.repository.JpaRepository;


import com.kodlamaio.rentACar.entities.concretes.Rental;

public interface RentalRepository extends JpaRepository<Rental,Integer> {
	Rental findById(int id);
	
}
