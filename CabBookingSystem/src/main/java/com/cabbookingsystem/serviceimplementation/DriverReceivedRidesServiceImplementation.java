package com.cabbookingsystem.serviceimplementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.DriverReceivedRides;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.RideStatus;
import com.cabbookingsystem.repository.DriverReceivedRidesRepository;
import com.cabbookingsystem.repository.RideRepository;
import com.cabbookingsystem.repository.RideStatusRepository;
import com.cabbookingsystem.service.DriverReceivedRidesService;

@Service
public class DriverReceivedRidesServiceImplementation implements DriverReceivedRidesService {

	@Autowired
	private DriverReceivedRidesRepository driverReceivedRidesRepository;

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private RideStatusRepository rideStatusRepository;

	@Scheduled(fixedRate = 60000) // Run every 1 minute (adjust as needed)
	public void checkRideTimeouts() {
		LocalDateTime currentTime = LocalDateTime.now();
		List<DriverReceivedRides> pendingReceivedRides = driverReceivedRidesRepository
				.findByResponseStatusAndReceivedAtBefore("No Response", currentTime.minusMinutes(2));

		for (DriverReceivedRides driverReceivedRide : pendingReceivedRides) {
			// Update the status to "TIMED_OUT"
			driverReceivedRide.setResponseStatus("TIMED_OUT");
			driverReceivedRidesRepository.save(driverReceivedRide);
			
//			Long value = driverReceivedRidesRepository
//					.countReceivedRidesWithStatus(driverReceivedRide.getRide().getRideId());
//			System.out.println(value);
//
//			if (value == 0) {
//				Ride ride = driverReceivedRide.getRide();
//				ride.setStatus("Driver Unavailable");
//				rideRepository.save(ride);
//
//				// Database Triger to save the ride status details to the RideStatus table
//				// simultaneously
//				RideStatus rideStatus = new RideStatus();
//				rideStatus.setRideId(ride.getRideId());
//				rideStatus.setStatus("Driver Unavailable");
//				rideStatus.setStatusUpdateTime(LocalDateTime.now());
//				rideStatus.setSourceName(ride.getSourceName());
//				rideStatus.setSourceName(ride.getDestinationName());
//				rideStatusRepository.save(rideStatus);
//			}
		}

	}
}
