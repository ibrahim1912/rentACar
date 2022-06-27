package com.kodlamaio.rentACar.entities.concretes;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "ordered_additional_Items")

public class OrderedAdditionalItem { //OrderedAdditionalItem
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@Column(name="total_day")  
	private int totalDay;
	
	@Column(name="pick_up_date")
	private LocalDate pickUpDate;
	
	@Column(name="return_date")
	private LocalDate returnDate;
	
	@ManyToOne
	@JoinColumn(name = "rental_id") // id ye göre rental tablosu
	private Rental rental;
	
	@ManyToOne
	@JoinColumn(name = "additional_item_id") // id ye göre additionalItem tablosu
	private AdditionalItem additionalItem;
	
//	@OneToMany(mappedBy = "additionalFeatureService")
//	private List<Invoice> invoices;
	
}
