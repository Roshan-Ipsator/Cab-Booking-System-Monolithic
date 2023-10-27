package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleModelRecord;

public interface VehicleModelService {
	public ServiceResponse<VehicleModel> addVehicleModel(AddVehicleModelRecord addVehicleModelRecord);
}
