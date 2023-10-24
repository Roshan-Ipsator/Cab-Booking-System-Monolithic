package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.Vehicle;
import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleRecord;

public interface VehicleService {
	public ServiceResponse<Vehicle> addVehicle(AddVehicleRecord addVehicleRecord);

	public ServiceResponse<Vehicle> assignVehicleModelToVehicle(Long vehicleModelId, Long vehicleId);
}
