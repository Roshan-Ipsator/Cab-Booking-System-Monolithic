package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.AddVehicleModelRecord;
import com.cabbookingsystem.record.AssignVehicleTypeToVehicleModelRecord;
import com.cabbookingsystem.record.CreateLocationRecord;
import com.cabbookingsystem.service.VehicleModelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicle-model")
public class VehicleModelController {

	@Autowired
	private VehicleModelService vehicleModelService;

	@PostMapping
	public ResponseEntity<ApiResponse> addVehicleModel(
			@Valid @RequestBody AddVehicleModelRecord addVehicleModelRecord) {
		return vehicleModelService.addVehicleModel(addVehicleModelRecord).finalResponse();
	}

}
