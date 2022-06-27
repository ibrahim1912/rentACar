package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.AdditionalItem;

public interface AdditionalItemRepository extends JpaRepository<AdditionalItem, Integer> {

	AdditionalItem findByName(String name);
}
