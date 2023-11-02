package com.cabbookingsystem.service;

import java.util.List;

import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.record.ChangePaymentTypeAndModeRecord;
import com.cabbookingsystem.record.CompleteRideRecord;

public interface RideService {
	public ServiceResponse<Ride> bookRide(BookRideRecord bookRideRecord);

	public ServiceResponse<List<User>> sendRideRequestToDrivers(Long rideId);

	public ServiceResponse<Ride> completeRide(CompleteRideRecord completeRideRecord);

	public ServiceResponse<Ride> enRouteRide(Long rideId);

	public ServiceResponse<Ride> makeRideStatusInProgress(Long rideId);

	public ServiceResponse<Ride> changePaymentTypeAndMode(
			ChangePaymentTypeAndModeRecord changePaymentTypeAndModeRecord);

}
