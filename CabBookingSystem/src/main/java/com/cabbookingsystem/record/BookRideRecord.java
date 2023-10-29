package com.cabbookingsystem.record;

import jakarta.validation.constraints.Pattern;

public record BookRideRecord(String sourceName, String destinationName, String paymentType, String vehicleType,
		@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Invalid date-time format. Correct Format is: yyyy-MM-dd HH:mm:ss") String rideStartTime) {

}
