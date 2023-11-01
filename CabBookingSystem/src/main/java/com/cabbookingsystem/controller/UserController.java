package com.cabbookingsystem.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.AssignVehicleToDriverRecord;
import com.cabbookingsystem.record.ChangeDestinationRecord;
import com.cabbookingsystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * API end point for getting username of current authenticated user
	 * 
	 * @param principal a Principal obejct
	 * @return ResponseEntity object
	 */
	@GetMapping
	public ResponseEntity<String> getUserName(Principal principal) {
		return new ResponseEntity<>("Currently authenticated user's username: " + principal.getName(), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ApiResponse> assignVehicleToDriver(
			@Valid @RequestBody AssignVehicleToDriverRecord assignVehicleToDriverRecord) {
		return userService.assignVehicleToDriver(assignVehicleToDriverRecord).finalResponse();
	}

	@PutMapping("ride-driver/{rideId}/{driverId}")
	public ResponseEntity<ApiResponse> assignRideToDriver(@PathVariable Long rideId, @PathVariable Long driverId) {
		return userService.assignRideToDriver(rideId, driverId).finalResponse();
	}

	@PutMapping("driver-status-available/{currentLocationName}")
	public ResponseEntity<ApiResponse> setDriverStatusToAvailable(@PathVariable String currentLocationName) {
		return userService.setDriverStatusToAvailable(currentLocationName).finalResponse();
	}

	@PutMapping("driver-accept-ride/{rideId}")
	public ResponseEntity<ApiResponse> acceptRide(@PathVariable Long rideId) {
		return userService.acceptRideRequest(rideId).finalResponse();
	}

	@PutMapping("pick-up-passenger/{rideId}/{otp}")
	public ResponseEntity<ApiResponse> pickUpPassenger(@PathVariable Long rideId, @PathVariable String otp) {
		return userService.pickUpPassenger(rideId, otp).finalResponse();
	}

	@PutMapping("/change-destination")
	public ResponseEntity<ApiResponse> changeDestinationDuringRide(
			@RequestBody ChangeDestinationRecord changeDestinationRecord) {
		return userService.changeDestinationDuringRide(changeDestinationRecord).finalResponse();
	}

	@GetMapping("/get-all-vehicleType-fare")
	public ResponseEntity<ApiResponse> getVehicleTypeWithFareForRide(@RequestParam String sourceName,
			@RequestParam String destinationName, @RequestParam String rideStartTime) {
		return userService.getVehicleTypeWithFareForRide(sourceName, destinationName, rideStartTime).finalResponse();
	}

	@PutMapping("driver-reject-ride/{rideId}")
	public ResponseEntity<ApiResponse> rejectRideRequest(@PathVariable Long rideId) {
		return userService.rejectRideRequest(rideId).finalResponse();
	}

}