package com.cabbookingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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

	private Double actualFare;

	private String paymentType; // Prepaid or Postpaid

	private String paymentMode;

	private Double paidAmount;

//    private RideStatus status;
	private String status;

	private String rideOtp;

//	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format. Correct Format is: yyyy-MM-dd HH:mm:ss")
	private LocalDateTime rideStartTime;

//	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format. Correct Format is: yyyy-MM-dd HH:mm:ss")
	private LocalDateTime rideEndTime;

	@ManyToOne // Unidirectional
	private VehicleType vehicleType;

	@ManyToOne(cascade = CascadeType.ALL) // Bidirectional
	private User passenger;

	@ManyToOne // Unidirectional
	private User driver;

}
