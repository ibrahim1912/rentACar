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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")// Arabaların tablosu
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler","maintenances","rentals"})

public class Car {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id") //Tablonun id alanı
	private int id;
	
	@Column(name="description") //Açıklama
	private String description;
	
	@Column(name="dailyPrice") //Arabanın günlük fiyatı
	private double dailyPrice;
	
	@Column(name="plate") // Arabanın plakası
	private String plate;
	
	@Column(name="kilometer") // Arabanın kilometresi
	private int kilometer;
	
	@Column(name="state") //1 Available 2 Maintenance 3 Rent
	private int state;
	
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
