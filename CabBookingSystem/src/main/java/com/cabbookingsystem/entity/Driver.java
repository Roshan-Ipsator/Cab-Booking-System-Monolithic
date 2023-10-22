package com.cabbookingsystem.entity;

import java.util.List;

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
@Table(name = "drivers")
public class Driver {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long driverId;

//	 @OneToOne
//	    @JoinColumn(name = "user_id")
//	    private User user;

//	 @OneToOne
//	    @JoinColumn(name = "vehicle_id")
//	    private Vehicle vehicle;

//	@Enumerated(EnumType.STRING)
//    private AvailabilityStatus availabilityStatus;

	private double rating;

}
