package com.cabbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.DriverAdditionalInfo;
import com.cabbookingsystem.entity.User;

@Repository
public interface DriverAdditionalInfoRepository extends JpaRepository<DriverAdditionalInfo, Long> {
	DriverAdditionalInfo findByDriver(User driver);
}
