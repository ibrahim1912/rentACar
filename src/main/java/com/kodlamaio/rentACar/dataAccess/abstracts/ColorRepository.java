package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.Color;

public interface ColorRepository extends JpaRepository<Color, Integer>{

	Color findById(int id);
}
