package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.AddVehicleRecord;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.record.ChangePaymentTypeAndModeRecord;
import com.cabbookingsystem.record.CompleteRideRecord;
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

	@GetMapping("/{rideId}")
	public ResponseEntity<ApiResponse> sendRideRequestToDrivers(@PathVariable Long rideId) {
		return rideService.sendRideRequestToDrivers(rideId).finalResponse();
	}

	@PutMapping("complete-ride")
	public ResponseEntity<ApiResponse> completeRide(@RequestBody CompleteRideRecord completeRideRecord) {
		return rideService.completeRide(completeRideRecord).finalResponse();
	}

	@PutMapping("enroute-ride")
	public ResponseEntity<ApiResponse> enRouteRide(@PathVariable Long rideId) {
		return rideService.enRouteRide(rideId).finalResponse();
	}

	@PutMapping("progress-ride")
	public ResponseEntity<ApiResponse> makeRideStatusInProgress(@PathVariable Long rideId) {
		return rideService.makeRideStatusInProgress(rideId).finalResponse();
	}

	@PutMapping("update-ride-payment-info")
	public ResponseEntity<ApiResponse> changePaymentTypeAndMode(
			@RequestBody ChangePaymentTypeAndModeRecord changePaymentTypeAndModeRecord) {
		return rideService.changePaymentTypeAndMode(changePaymentTypeAndModeRecord).finalResponse();
	}
}
