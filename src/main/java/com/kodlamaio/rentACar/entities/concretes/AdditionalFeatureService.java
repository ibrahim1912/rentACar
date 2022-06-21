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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "additional_feature_services")

public class AdditionalFeatureService {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@Column(name="total_day")  
	private int totalDay;
	
	@ManyToOne
	@JoinColumn(name = "rental_id") // id ye göre rental tablosu
	private Rental rental;
	
	@ManyToOne
	@JoinColumn(name = "additional_feature_item_id") // id ye göre addionalFeatureItem tablosu
	private AdditionalFeatureItem additionalFeatureItem;
	
//	@OneToMany(mappedBy = "additionalFeatureService")
//	private List<Invoice> invoices;
	
}
