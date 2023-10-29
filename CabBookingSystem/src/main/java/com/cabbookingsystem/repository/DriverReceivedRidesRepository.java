package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.DriverReceivedRides;

@Repository
public interface DriverReceivedRidesRepository extends JpaRepository<DriverReceivedRides, Long> {

}
