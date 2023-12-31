package com.cabbookingsystem.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.RideStatus;

@Repository
public interface RideStatusRepository extends JpaRepository<RideStatus, Long> {

	@Query("SELECT rs FROM RideStatus rs " + "WHERE rs.rideId = :rideId " + "AND rs.status = :status "
			+ "ORDER BY rs.statusUpdateTime DESC LIMIT 1")
	Optional<RideStatus> findMostRecentByRideIdAndStatus(Long rideId, String status);

	@Query("SELECT r.statusUpdateTime FROM RideStatus r " + "WHERE r.rideId = :rideId " + "AND r.status = 'Accepted'")
	LocalDateTime findAcceptedTimeByRideId(Long rideId);

	@Query("SELECT rs.statusUpdateTime FROM RideStatus rs " + "WHERE rs.rideId = :rideId "
			+ "AND rs.status = 'Completed'")
	LocalDateTime findCompletedStatusTimeByRideId(Long rideId);

}
