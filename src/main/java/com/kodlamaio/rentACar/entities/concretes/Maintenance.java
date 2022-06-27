package com.kodlamaio.rentACar.entities.concretes;

import java.time.LocalDate;
import java.util.Date;

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

@Entity
@Table(name = "maintenances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name = "sent_date")
	private LocalDate sentDate;
	
	@Column(name = "returned_date")
	private LocalDate returnedDate;
	
	@ManyToOne
	@JoinColumn(name ="car_id")
	private Car car;
}
