package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;

public interface VehicleModelService {
	public ServiceResponse<VehicleModel> addVehicleModel(VehicleModel vehicleModel);
}
