package com.cabbookingsystem.record;

public record AssignVehicleToDriverRecord(Long driverId, String vehicleRegistrationNumber, Long vehicleModelId,
		Integer vehicleTypeId) {

}
