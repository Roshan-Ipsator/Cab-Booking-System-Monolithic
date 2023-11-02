package com.cabbookingsystem.entity;

import java.time.LocalDateTime;

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
public class RideStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long statusId;

	private Long rideId;

	private String status;

	private LocalDateTime statusUpdateTime;

	private String sourceName;

	private Double sourceLatitude;

	private Double sourceLongitude;

	private String destName;

	private Double destLatitude;

	private Double destLongitude;
}
