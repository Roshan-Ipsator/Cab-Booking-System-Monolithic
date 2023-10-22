package com.cabbookingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
	
//	@ManyToOne
//    @JoinColumn(name = "driver_id")
//    private Driver driver;
	
//	@ManyToOne
//    @JoinColumn(name = "start_location_id")
//    private Location startLocation;
	
//	@ManyToOne
//    @JoinColumn(name = "end_location_id")
//    private Location endLocation;
	
	private double fare;
//    private RideStatus status;
//    private Date rideDate;




}
