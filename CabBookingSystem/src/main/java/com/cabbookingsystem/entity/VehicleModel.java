package com.cabbookingsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "vehicle_models")
public class VehicleModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long modelId;

	@Column(unique = true, nullable = false)
	private String modelName;

	private String brand;

	private String modelDescription;

//	@ManyToOne // Unidirectional
//	private VehicleType vehicleType;
}
