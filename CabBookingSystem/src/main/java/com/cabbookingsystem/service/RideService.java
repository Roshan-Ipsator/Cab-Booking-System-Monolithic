package com.cabbookingsystem.service;

import java.util.List;

import com.cabbookingsystem.entity.RatingAndFeedback;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.record.ChangePaymentTypeAndModeRecord;
import com.cabbookingsystem.record.CompleteRideRecord;
import com.cabbookingsystem.record.MakePaymentRecord;
import com.cabbookingsystem.record.RatingFeedbackRecord;

public interface RideService {
	public ServiceResponse<Ride> bookRide(BookRideRecord bookRideRecord);

	public ServiceResponse<List<User>> sendRideRequestToDrivers(Long rideId);

	public ServiceResponse<Ride> completeRide(CompleteRideRecord completeRideRecord);

	public ServiceResponse<Ride> enRouteRide(Long rideId);

	public ServiceResponse<Ride> makeRideStatusInProgress(Long rideId);

	public ServiceResponse<Ride> changePaymentTypeAndMode(
			ChangePaymentTypeAndModeRecord changePaymentTypeAndModeRecord);

	public ServiceResponse<Ride> cancelRideByDriver(Long rideId);

	public ServiceResponse<Ride> cancelRideByPassenger(Long rideId);

	public ServiceResponse<Ride> makePaymentForRide(MakePaymentRecord makePaymentRecord);

	public ServiceResponse<RatingAndFeedback> giveRatingFeedback(RatingFeedbackRecord ratingFeedbackRecord);

}
