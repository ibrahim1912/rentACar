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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name="invoices")
public class Invoice {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="invoice_number")
	private int invoiceNumber;
	
	@Column(name="current_date")
	private LocalDate currentDate;
	
	@Column(name="total_price")
	private double totalPrice;
	
	@ManyToOne
	@JoinColumn(name = "rentalId")
	private Rental rental;
	
	@Column(name="status")
	private int status;  // 0 invoice var -- 1 invoice iptal edilmiş
	
//	@ManyToOne
//	@JoinColumn(name = "additionalFeatureServiceId")
//	private AdditionalFeatureService additionalFeatureService;
}
