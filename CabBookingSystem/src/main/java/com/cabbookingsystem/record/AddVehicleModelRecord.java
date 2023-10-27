package com.cabbookingsystem.record;

import jakarta.persistence.Column;

public record AddVehicleModelRecord(@Column(unique = true, nullable = false) String modelName, String brand,
		String modelDescription) {

}
