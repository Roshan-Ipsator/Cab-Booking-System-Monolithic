package com.cabbookingsystem.service;

import java.util.List;

import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleRecord;
import com.cabbookingsystem.record.BookRideRecord;

public interface RideService {
	public ServiceResponse<Ride> bookRide(BookRideRecord bookRideRecord);

	public ServiceResponse<List<User>> sendRideRequestToDrivers(Long rideId);
	
}
