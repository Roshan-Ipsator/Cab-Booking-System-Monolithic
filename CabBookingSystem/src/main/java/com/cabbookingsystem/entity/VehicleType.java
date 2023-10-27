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
@Table(name = "vehicle_types")
public class VehicleType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeId;
	private String typeName;
	private Integer seatingCapacity;
	private Double pricePerKm;
	private String typeDescription;
}
