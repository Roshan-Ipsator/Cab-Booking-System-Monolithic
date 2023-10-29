package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.UserCredits;

@Repository
public interface UserCreditsRepository extends JpaRepository<UserCredits, Long> {
	UserCredits findByUserUserId(Long userId);
}
