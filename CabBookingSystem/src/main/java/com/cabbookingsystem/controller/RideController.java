package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.AddVehicleRecord;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.service.RideService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ride")
public class RideController {

	@Autowired
	private RideService rideService;

	@PostMapping
	public ResponseEntity<ApiResponse> bookRide(@Valid @RequestBody BookRideRecord bookRideRecord) {
		return rideService.bookRide(bookRideRecord).finalResponse();
	}
}
