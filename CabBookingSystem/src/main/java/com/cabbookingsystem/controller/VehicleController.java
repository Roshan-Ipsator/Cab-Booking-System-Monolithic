package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.AddVehicleRecord;
import com.cabbookingsystem.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	@Autowired
	private VehicleService vehicleService;

	@PostMapping
	public ResponseEntity<ApiResponse> addVehicleModel(@Valid @RequestBody AddVehicleRecord addVehicleRecord) {
		return vehicleService.addVehicle(addVehicleRecord).finalResponse();
	}

	@PutMapping("{vehicleModelId}/{vehicleId}")
	public ResponseEntity<ApiResponse> assignVehicleModelToVehicle(@PathVariable Long vehicleModelId,
			@PathVariable Long vehicleId) {
		return vehicleService.assignVehicleModelToVehicle(vehicleModelId, vehicleId).finalResponse();
	}
}
