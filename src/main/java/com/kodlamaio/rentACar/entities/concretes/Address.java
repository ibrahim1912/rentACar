package com.kodlamaio.rentACar.entities.concretes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "contact_address")
	private String contactAddress;

	@Column(name = "invoice_address")
	private String invoiceAddress;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
//	@OneToOne
//	@JoinColumn(name = "town_id")
//	private Town town;
	
//	@ManyToOne
//	@JoinColumn(name = "city_id")
//	private City city;

	

}
