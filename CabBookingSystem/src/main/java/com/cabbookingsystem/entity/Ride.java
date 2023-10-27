package com.cabbookingsystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "rides")
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rideId;

	private String sourceName;

	private Double sourceLatitude;

	private Double sourceLongitude;

	private String destinationName;

	private Double destinationLatitude;

	private Double destinationLongitude;

	private Double estimatedFare;

	private String paymentType; // Prepaid or Postpaid

//    private RideStatus status;
	private String status;

	private LocalDateTime rideStartTime;

	private LocalDateTime rideEndTime;

	@ManyToOne(cascade = CascadeType.ALL) // Bidirectional
	private User passenger;

	@ManyToOne // Unidirectional
	private User driver;

	@OneToOne(cascade = CascadeType.ALL) // Unidirectional
	private Payment payment;

}
