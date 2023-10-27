package com.cabbookingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.entity.VehicleType;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {
	Optional<VehicleType> findByTypeName(String typeName);
}
