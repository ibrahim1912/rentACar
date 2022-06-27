package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.CorporateCustomer;

public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Integer> {

	CorporateCustomer findByCorporateCustomerId(int id);
	CorporateCustomer findByCompanyName(String companyName);
}
