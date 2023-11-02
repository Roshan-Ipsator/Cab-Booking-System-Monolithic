package com.cabbookingsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.DriverReceivedRides;
import com.cabbookingsystem.entity.Ride;

@Repository
public interface DriverReceivedRidesRepository extends JpaRepository<DriverReceivedRides, Long> {
	@Query("SELECT rr FROM DriverReceivedRides rr " + "WHERE rr.driver.userId = :driverId "
			+ "AND rr.ride.rideId = :rideId")
	DriverReceivedRides findReceivedRidesByDriverAndRideIds(Long driverId, Long rideId);

	List<DriverReceivedRides> findByRide(Ride ride);

	List<DriverReceivedRides> findByRideRideId(Long rideId);

	List<DriverReceivedRides> findByResponseStatusAndReceivedAtBefore(String status, LocalDateTime time);

	@Query("SELECT COUNT(rr) FROM DriverReceivedRides rr " + "WHERE rr.ride.rideId = :rideId "
			+ "AND rr.responseStatus NOT IN ('Timed Out', 'Rejected')")
	Long countReceivedRidesWithStatus(Long rideId);

	@Query("SELECT COUNT(rr) FROM DriverReceivedRides rr " + "WHERE rr.driver.userId = :driverId "
			+ "AND rr.responseStatus = 'Accepted'")
	Long countAcceptedRideRequestsByDriverId(Long driverId);

	@Query("SELECT COUNT(rr) FROM DriverReceivedRides rr " + "WHERE rr.driver.userId = :driverId")
	Long countAllRideRequestsByDriverId(Long driverId);
}
