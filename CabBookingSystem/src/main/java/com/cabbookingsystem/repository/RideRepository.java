package com.cabbookingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

}
