package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

	Address findById(int id);
}
