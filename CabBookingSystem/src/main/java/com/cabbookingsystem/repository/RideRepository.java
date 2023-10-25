package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

}
