package com.kodlamaio.rentACar.entities.concretes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cities") // Sehir Tablosu
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name="id") //Şehrin id si
	private int id;
	
	@Column(name="name") // Şehrin ismi
	private String name;

	@OneToMany(mappedBy = "city") // Bir şehrin bir çok arabası olabililr
	private List<Car> cars;
	
	
}
