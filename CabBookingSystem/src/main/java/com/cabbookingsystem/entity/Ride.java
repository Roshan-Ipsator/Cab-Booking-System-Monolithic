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

//	@ManyToOne
//	@JoinColumn(name = "customer_id")
//	private Customer customer;
//	
//	@ManyToOne
//	@JoinColumn(name = "admin_id")
//	private Admin admin;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;

	@ManyToOne
	@JoinColumn(name = "start_location_id")
	private Location startLocation;

	@ManyToOne
	@JoinColumn(name = "end_location_id")
	private Location endLocation;

	private double fare;
//    private RideStatus status;
	private String status;
	private LocalDateTime rideDateTime;

	@OneToOne(cascade = CascadeType.ALL)
	private Payment payment;

	@OneToMany(cascade = CascadeType.ALL)
	private List<RatingAndFeedback> ratingAndFeedbacks;

}
