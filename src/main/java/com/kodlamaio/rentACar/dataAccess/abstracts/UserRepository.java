package com.kodlamaio.rentACar.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentACar.entities.concretes.User;

public interface UserRepository  extends JpaRepository<User,Integer>{

	User findById(int id);
}
