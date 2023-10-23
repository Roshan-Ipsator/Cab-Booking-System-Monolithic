package com.cabbookingsystem.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverAdditionalInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long infoId;

	private String availabilityStatus;

	private double rating;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "driver_id")
	private User driver;

}
