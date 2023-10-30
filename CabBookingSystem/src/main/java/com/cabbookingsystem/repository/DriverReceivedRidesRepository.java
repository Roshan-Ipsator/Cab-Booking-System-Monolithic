package com.cabbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.DriverReceivedRides;

@Repository
public interface DriverReceivedRidesRepository extends JpaRepository<DriverReceivedRides, Long> {
	@Query("SELECT rr FROM DriverReceivedRides rr " + "WHERE rr.driver.userId = :driverId "
			+ "AND rr.ride.rideId = :rideId")
	DriverReceivedRides findReceivedRidesByDriverAndRideIds(Long driverId, Long rideId);
}
