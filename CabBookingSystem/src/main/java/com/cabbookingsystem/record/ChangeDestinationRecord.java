package com.cabbookingsystem.record;

public record ChangeDestinationRecord(Long rideId, String newDestinationName, Double currentLatitude,
		Double currentLongitude) {

}
