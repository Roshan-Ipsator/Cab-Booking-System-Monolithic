package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ServiceResponse;

public interface VehicleTypeService {
	public ServiceResponse<VehicleType> addVehicleType(VehicleType vehicleType);
}
