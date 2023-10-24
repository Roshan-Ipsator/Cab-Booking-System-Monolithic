package com.cabbookingsystem.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicles")
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vehicleId;

	@Column(unique = true)
	private String registrationNumber;
	private Integer capacity;

	private String active;
	private String available;

	@ManyToOne(cascade = CascadeType.ALL)
	private VehicleModel vehicleModel;

	@OneToOne(cascade = CascadeType.ALL)  // Unidirectional
	@JoinColumn(name = "driver_id")
	private User driver;
}
