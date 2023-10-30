package com.cabbookingsystem.serviceimplementation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.DriverAdditionalInfo;
import com.cabbookingsystem.entity.DriverReceivedRides;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.RideStatus;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.entity.UserCredits;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.repository.DriverAdditionalInfoRepository;
import com.cabbookingsystem.repository.DriverReceivedRidesRepository;
import com.cabbookingsystem.repository.RideRepository;
import com.cabbookingsystem.repository.RideStatusRepository;
import com.cabbookingsystem.repository.UserCreditsRepository;
import com.cabbookingsystem.repository.UserRepository;
import com.cabbookingsystem.repository.VehicleTypeRepository;
import com.cabbookingsystem.service.RideService;
import com.cabbookingsystem.util.LocationUtils;

@Service
public class RideServiceImplementation implements RideService {

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	@Autowired
	private UserCreditsRepository userCreditsRepository;

	@Autowired
	private DriverAdditionalInfoRepository driverAdditionalInfoRepository;

	@Autowired
	private DriverReceivedRidesRepository driverReceivedRidesRepository;

	@Autowired
	private RideStatusRepository rideStatusRepository;

	@Override
	public ServiceResponse<Ride> bookRide(BookRideRecord bookRideRecord) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			Optional<VehicleType> vehicleTypeOptional = vehicleTypeRepository
					.findByTypeName(bookRideRecord.vehicleType().toLowerCase());

			if (vehicleTypeOptional.isPresent()) {
				VehicleType selectedVehicleType = vehicleTypeOptional.get();

				double[] startLocationValues = LocationUtils.generateLocationCoordinates();
				Double startLocationLatitude = startLocationValues[0];
				Double startLocationLongitude = startLocationValues[1];

				double[] endLocationValues = LocationUtils.generateLocationCoordinates();
				Double endLocationLatitude = endLocationValues[0];
				Double endLocationLongitude = endLocationValues[1];

				Double distance = LocationUtils.calculateDistance(startLocationLatitude, startLocationLongitude,
						endLocationLatitude, endLocationLongitude);

				Double estimatedFarePrice = distance * selectedVehicleType.getPricePerKm();

				// Check the user's payment type
				if (bookRideRecord.paymentType().equalsIgnoreCase("Prepaid")) {
					// Check the user's credit amount
					UserCredits userCredits = userCreditsRepository.findByUserUserId(currentLoggedInUser.getUserId());
					if (userCredits.getCurrentBalance() < estimatedFarePrice) {
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"Insufficient balance in the user's credit account. Please, add atleast an amount of "
										+ (estimatedFarePrice - userCredits.getCurrentBalance())
										+ " or choose Postpaid payment type!");
						return response;
					}
				}

				Ride newRide = new Ride();
				newRide.setSourceName(bookRideRecord.sourceName());
				newRide.setSourceLatitude(startLocationLatitude);
				newRide.setSourceLongitude(startLocationLongitude);
				newRide.setDestinationName(bookRideRecord.destinationName());
				newRide.setDestinationLatitude(endLocationLatitude);
				newRide.setDestinationLongitude(endLocationLongitude);
				newRide.setEstimatedFare(estimatedFarePrice);
				newRide.setPaymentType(bookRideRecord.paymentType());
				newRide.setStatus("Booked");

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime rideStartTime = LocalDateTime.parse(bookRideRecord.rideStartTime(), formatter);

				newRide.setRideStartTime(rideStartTime);

				newRide.setVehicleType(selectedVehicleType);
				newRide.setPassenger(currentLoggedInUser);

				Ride bookedRide = rideRepository.save(newRide);

				// Database Triger to save the ride status details to the RideStatus table
				// simultaneously
				RideStatus rideStatus = new RideStatus();
				rideStatus.setRideId(bookedRide.getRideId());
				rideStatus.setStatus("Booked");
				rideStatus.setStatusUpdateTime(LocalDateTime.now());
				rideStatusRepository.save(rideStatus);

				ServiceResponse<Ride> response = new ServiceResponse<>(true, bookedRide, "Ride booked successfully!");
				return response;

			}

			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Invalid vehicle type name: " + bookRideRecord.vehicleType() + ". Please, try with a valid one!");
			return response;

		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<List<User>> sendRideRequestToDrivers(Long rideId) {
		Optional<Ride> rideOptional = rideRepository.findById(rideId);
		if (rideOptional.isPresent()) {
			Ride existingRide = rideOptional.get();

			List<DriverAdditionalInfo> driverAdditionalInfos = driverAdditionalInfoRepository
					.findTopDriversInfoWithinRadius(existingRide.getSourceLongitude(), existingRide.getSourceLatitude(),
							13000000, existingRide.getVehicleType().getTypeName());

			if (driverAdditionalInfos.size() != 0) {
				List<User> selectedDrivers = new ArrayList<>();

				for (DriverAdditionalInfo driverAdditionalInfo : driverAdditionalInfos) {
					selectedDrivers.add(driverAdditionalInfo.getDriver());

					DriverReceivedRides driverReceivedRides = new DriverReceivedRides();
					driverReceivedRides.setResponseStatus("No Response");
					driverReceivedRides.setReceivedAt(LocalDateTime.now());
					driverReceivedRides.setDriver(driverAdditionalInfo.getDriver());
					driverReceivedRides.setRide(existingRide);

					driverReceivedRidesRepository.save(driverReceivedRides);
				}

				ServiceResponse<List<User>> response = new ServiceResponse<>(true, selectedDrivers,
						"List of selected drivers returned successfully!");
				return response;
			}
			ServiceResponse<List<User>> response = new ServiceResponse<>(false, null,
					"No driver is available, now. Please, try after some time");
			return response;
		}
		ServiceResponse<List<User>> response = new ServiceResponse<>(false, null,
				"Invalid ride id: " + rideId + ". Please, try again with a valid id!");
		return response;
	}

}
