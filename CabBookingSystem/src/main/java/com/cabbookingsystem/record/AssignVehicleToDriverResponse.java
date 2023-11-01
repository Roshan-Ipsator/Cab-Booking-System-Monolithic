package com.cabbookingsystem.record;

public record AssignVehicleToDriverResponse(Long driverId, String driverFirstName, String vehicleRegistrationNumber,
		String modelName, String typeName) {

}
