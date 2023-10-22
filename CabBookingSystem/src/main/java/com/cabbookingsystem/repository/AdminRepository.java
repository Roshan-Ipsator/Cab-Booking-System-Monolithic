package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.Admin;
import com.cabbookingsystem.entity.Customer;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
