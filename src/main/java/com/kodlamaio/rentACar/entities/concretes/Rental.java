package com.kodlamaio.rentACar.entities.concretes;


import java.time.LocalDate;
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
@Table(name="rentals")
public class Rental {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="pick_up_date")
	private LocalDate pickUpDate;
	
	@Column(name="return_date")
	private LocalDate returnDate;
	
	@Column(name="total_days")
	private int totalDays;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@ManyToOne
	@JoinColumn(name ="car_id")
	private Car car;
	 
	@ManyToOne  //Hangi şehirde arabayı kiraladık
	@JoinColumn(name="pick_up_city_id",referencedColumnName="id")
	private City pickUpCityId;
	
	@ManyToOne //Hangi şehirde arabayı teslim ettik
	@JoinColumn(name="return_city_id",referencedColumnName="id")
	private City returnCityId;
	
	@OneToMany(mappedBy = "rental")
	private List<OrderedAdditionalItem> orderedAdditionalItems;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "rental")
	private List<Invoice> invoices;
	
}
