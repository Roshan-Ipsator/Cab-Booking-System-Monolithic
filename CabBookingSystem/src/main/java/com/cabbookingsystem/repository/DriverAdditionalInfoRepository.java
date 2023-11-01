package com.cabbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.DriverAdditionalInfo;
import com.cabbookingsystem.entity.User;

@Repository
public interface DriverAdditionalInfoRepository extends JpaRepository<DriverAdditionalInfo, Long> {
	DriverAdditionalInfo findByDriver(User driver);

	@Query("SELECT dinfo FROM DriverAdditionalInfo dinfo " + "WHERE dinfo.availabilityStatus = 'Available' "
			+ "AND dinfo.vehicleType.typeName = :vehicleTypeName "
			+ "AND ST_Distance_Sphere(POINT(dinfo.currentLongitude, dinfo.currentLatitude), "
			+ "POINT(:sourceLongitude, :sourceLatitude)) <= :maxDistance "
			+ "ORDER BY ST_Distance_Sphere(POINT(dinfo.currentLongitude, dinfo.currentLatitude), "
			+ "POINT(:sourceLongitude, :sourceLatitude)), "
			+ "0.65 * COALESCE(dinfo.averageRating, 0) + 0.35 * COALESCE(dinfo.rideAcceptanceRate, 0) DESC")
	List<DriverAdditionalInfo> findTopDriversInfoWithinRadius(double sourceLongitude, double sourceLatitude,
			double maxDistance, String vehicleTypeName);
}
