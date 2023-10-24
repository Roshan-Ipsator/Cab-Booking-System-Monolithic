package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.Location;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.CreateLocationRecord;

public interface LocationService {
	public ServiceResponse<Location> createLocation(CreateLocationRecord createLocationRecord);
}
