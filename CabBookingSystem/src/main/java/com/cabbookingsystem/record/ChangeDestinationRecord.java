package com.cabbookingsystem.record;

public record ChangeDestinationRecord(Long rideId, String newDestinationName, String currentLocationName,
		Double currentLatitude, Double currentLongitude) {

}
