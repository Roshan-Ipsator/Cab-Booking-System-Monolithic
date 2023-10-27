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
@Table(name = "favourite_addresses")
public class FavouriteLocations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locationId;
	private String locationName;
	private String district;
	private String state;
	private String country;
	private String pincode;
	private Double latitude;
	private Double longitude;
}
