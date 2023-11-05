package com.cabbookingsystem.service;

import java.util.List;

import com.cabbookingsystem.entity.DriverAdditionalInfo;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AssignVehicleToDriverRecord;
import com.cabbookingsystem.record.AssignVehicleToDriverResponse;
import com.cabbookingsystem.record.ChangeDestinationRecord;
import com.cabbookingsystem.record.GiveTipRecord;
import com.cabbookingsystem.record.LoginUserRecord;
import com.cabbookingsystem.record.SetProfileDetailsRecord;
import com.cabbookingsystem.record.VehicleTypeFareRecord;

import jakarta.mail.MessagingException;

public interface UserService {

	/**
	 * 
	 * The method to send a verification email for the final login
	 * 
	 * @param loginUserRecord object of LoginUserRecord contains the user's email id
	 * 
	 * @return loginKey a string to verify the user for final login
	 * 
	 * @throws UserException, MessagingException
	 * 
	 */
	public ServiceResponse<String> preFinalUserLogin(LoginUserRecord loginUserRecord) throws MessagingException;

	/**
	 * 
	 * The method for the final login after final login verification
	 * 
	 * @param loginKey a string to verify the user for final login
	 * 
	 * @return User object
	 * 
	 * @throws UserException
	 * 
	 */
	public ServiceResponse<String> finalUserLogin(String loginKey);

	/**
	 * Retrieves a list of all users from the service.
	 *
	 * This method sends a request to the service to retrieve a list of all users
	 * currently available in the system. The result is encapsulated within a
	 * {@link ServiceResponse} object, which provides information about the
	 * operation's success or failure and the retrieved user data.
	 *
	 * @return A {@link ServiceResponse} containing a list of users if the operation
	 *         is successful. If the operation fails, the response will indicate the
	 *         error.
	 *
	 * @see ServiceResponse
	 * @see User
	 */
	public ServiceResponse<List<User>> getAllUsers();

//	public ServiceResponse<User> setPhoneNumber(String phone);

	/**
	 * Sets and updates the profile details of a user based on the information
	 * provided in the {@code SetProfileDetailsRecord}.
	 *
	 * @param setProfileDetailsRecord The record containing the updated profile
	 *                                details for the user.
	 * @return A {@code ServiceResponse} representing the result of setting the
	 *         profile details. It encapsulates information about the success or
	 *         failure of the operation, any error messages, and the updated user
	 *         profile if the operation was successful.
	 */
	public ServiceResponse<User> setProfileDetails(SetProfileDetailsRecord setProfileDetailsRecord);

	/**
	 * Creates a new administrative user with full access privileges.
	 */
	public ServiceResponse<User> createAdminWithAllAccess();

	public ServiceResponse<AssignVehicleToDriverResponse> assignVehicleToDriver(
			AssignVehicleToDriverRecord assignVehicleToDriverRecord);

	public ServiceResponse<Ride> assignRideToDriver(Long rideId, Long driverId);

	public ServiceResponse<DriverAdditionalInfo> setDriverStatusToAvailable(String currentLocation);

	public ServiceResponse<Ride> acceptRideRequest(Long rideId) throws MessagingException;

	public ServiceResponse<Ride> pickUpPassenger(Long rideId, String otp);

	public ServiceResponse<Ride> changeDestinationDuringRide(ChangeDestinationRecord changeDestinationRecord);

	public ServiceResponse<List<VehicleTypeFareRecord>> getVehicleTypeWithFareForRide(String sourceName,
			String destinationName, String rideStartTime);

	public ServiceResponse<Ride> rejectRideRequest(Long rideId);

	public ServiceResponse<DriverAdditionalInfo> changeStatusToUnavailable();

	public ServiceResponse<String> giveTipToDriver(GiveTipRecord giveTipRecord);

	public ServiceResponse<List<Ride>> getBookingHistory(Long userId);

}
