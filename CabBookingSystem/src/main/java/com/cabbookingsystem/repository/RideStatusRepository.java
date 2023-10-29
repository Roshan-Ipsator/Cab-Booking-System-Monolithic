package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.RideStatus;

@Repository
public interface RideStatusRepository extends JpaRepository<RideStatus, Long> {

}
