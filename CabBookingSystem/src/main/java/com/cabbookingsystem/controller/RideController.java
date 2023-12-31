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
import com.cabbookingsystem.record.MakePaymentRecord;
import com.cabbookingsystem.record.RatingFeedbackRecord;
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

	@PutMapping("/complete-ride")
	public ResponseEntity<ApiResponse> completeRide(@RequestBody CompleteRideRecord completeRideRecord) {
		return rideService.completeRide(completeRideRecord).finalResponse();
	}

	@PutMapping("/enroute-ride/{rideId}")
	public ResponseEntity<ApiResponse> enRouteRide(@PathVariable Long rideId) {
		return rideService.enRouteRide(rideId).finalResponse();
	}

	@PutMapping("/progress-ride/{rideId}")
	public ResponseEntity<ApiResponse> makeRideStatusInProgress(@PathVariable Long rideId) {
		return rideService.makeRideStatusInProgress(rideId).finalResponse();
	}

	@PutMapping("/update-ride-payment-info")
	public ResponseEntity<ApiResponse> changePaymentTypeAndMode(
			@RequestBody ChangePaymentTypeAndModeRecord changePaymentTypeAndModeRecord) {
		return rideService.changePaymentTypeAndMode(changePaymentTypeAndModeRecord).finalResponse();
	}

	@PutMapping("/cancel-ride-by-driver/{rideId}")
	public ResponseEntity<ApiResponse> cancelRideByDriver(@PathVariable Long rideId) {
		return rideService.cancelRideByDriver(rideId).finalResponse();
	}

	@PutMapping("/cancel-ride-by-passenger/{rideId}")
	public ResponseEntity<ApiResponse> cancelRideByPassenger(@PathVariable Long rideId) {
		return rideService.cancelRideByPassenger(rideId).finalResponse();
	}

	@PutMapping("/make-payment-for-ride")
	public ResponseEntity<ApiResponse> makePaymentForRide(@RequestBody MakePaymentRecord makePaymentRecord) {
		return rideService.makePaymentForRide(makePaymentRecord).finalResponse();
	}

	@PutMapping("/give-rating-feedback")
	public ResponseEntity<ApiResponse> giveRatingFeedback(@RequestBody RatingFeedbackRecord ratingFeedbackRecord) {
		return rideService.giveRatingFeedback(ratingFeedbackRecord).finalResponse();
	}

}
