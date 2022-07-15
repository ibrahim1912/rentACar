package com.kodlamaio.rentACar.entities.concretes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="description") 
	private String description;
	
	@Column(name="dailyPrice") 
	private double dailyPrice;
	
	@Column(name="plate") 
	private String plate;
	
	@Column(name="kilometer") 
	private int kilometer;
	
	@Column(name="state") //1 Available 2 Maintenance 3 Rent
	private int state;
	
	@Column(name="min_findeks_score")
	private int minFindeksScore;
	
	@ManyToOne
	@JoinColumn(name = "brand_id") // id ye göre brand tablosu  // db ye id koyar
	private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "color_id") // id ye göre color tablosu
	private Color color;
	 
	@OneToMany(mappedBy = "car") // Bir araba bir çok kez bakıma gidebilir
	private List<Maintenance> maintenances;
	
	@OneToMany(mappedBy = "car") // Bir araba bir çok kez kiralanabilir
	private List<Rental> rentals;
	
	@ManyToOne //Bir şehirde birden fazla araba olabilir
	@JoinColumn(name="city_id")
	private City city;
	

	
	
	
}
