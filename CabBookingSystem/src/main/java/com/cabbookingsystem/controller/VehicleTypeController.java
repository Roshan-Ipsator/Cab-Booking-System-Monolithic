package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.service.VehicleModelService;
import com.cabbookingsystem.service.VehicleTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicle-type")
public class VehicleTypeController {
	@Autowired
	private VehicleTypeService vehicleTypeService;

	@PostMapping
	public ResponseEntity<ApiResponse> addVehicleType(@Valid @RequestBody VehicleType vehicleType) {
		return vehicleTypeService.addVehicleType(vehicleType).finalResponse();
	}
}
