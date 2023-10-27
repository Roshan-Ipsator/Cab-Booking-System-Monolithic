package com.cabbookingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverReceivedRides {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long receivedRideId;

	private String responseStatus;

	private LocalDateTime receivedAt;

	@ManyToOne // Unidirectional
	private Ride ride;

	@ManyToOne // Unidirectional
	private User driver;
}
