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

	private Double averageRating;

	private Double rideAcceptanceRate;

	@Column(unique = true)
	private String vehicleRegistrationNumber;

	private String currentLocationName;

	private Double currentLatitude;

	private Double currentLongitude;

	@ManyToOne(cascade = CascadeType.ALL)
	private VehicleModel vehicleModel;

	@OneToOne(cascade = CascadeType.ALL) // Unidirectional
	@JoinColumn(name = "driver_id")
	private User driver;

}
