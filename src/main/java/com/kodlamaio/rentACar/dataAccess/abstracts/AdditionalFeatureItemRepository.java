package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureItem;


public interface AdditionalFeatureItemRepository extends JpaRepository<AdditionalFeatureItem,Integer>{

	AdditionalFeatureItem findById(int id);
}
