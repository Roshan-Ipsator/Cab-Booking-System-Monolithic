package com.cabbookingsystem.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.service.UserService;

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

	@PutMapping("/{vehicleId}/{driverId}")
	public ResponseEntity<ApiResponse> assignVehicleToDriver(@PathVariable Long vehicleId,
			@PathVariable Long driverId) {
		return userService.assignVehicleToDriver(vehicleId, driverId).finalResponse();
	}

}