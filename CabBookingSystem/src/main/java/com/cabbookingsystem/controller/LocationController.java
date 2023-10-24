package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.entity.Location;
import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.CreateLocationRecord;
import com.cabbookingsystem.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@PostMapping
	public ResponseEntity<ApiResponse> createLocation(@Valid @RequestBody CreateLocationRecord createLocationRecord) {
		return locationService.createLocation(createLocationRecord).finalResponse();
	}
}
