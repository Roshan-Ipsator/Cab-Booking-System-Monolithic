package com.cabbookingsystem.serviceimplementation;

import java.time.Duration;
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
import com.cabbookingsystem.entity.RatingAndFeedback;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.RideStatus;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.entity.UserCredits;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.record.ChangePaymentTypeAndModeRecord;
import com.cabbookingsystem.record.CompleteRideRecord;
import com.cabbookingsystem.record.MakePaymentRecord;
import com.cabbookingsystem.record.RatingFeedbackRecord;
import com.cabbookingsystem.repository.DriverAdditionalInfoRepository;
import com.cabbookingsystem.repository.DriverReceivedRidesRepository;
import com.cabbookingsystem.repository.RatingAndFeedBackRepository;
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

	@Autowired
	private RatingAndFeedBackRepository ratingAndFeedBackRepository;

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

					if (!bookRideRecord.paymentMode().equalsIgnoreCase("Credits")) {
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"For Prepaid payment type, the payment mode must be Credits.");
						return response;
					}

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
				newRide.setPaymentType(bookRideRecord.paymentMode());
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
				rideStatus.setSourceName(bookedRide.getSourceName());
				rideStatus.setSourceLatitude(startLocationLatitude);
				rideStatus.setSourceLongitude(startLocationLongitude);
				rideStatus.setDestName(bookedRide.getDestinationName());
				rideStatus.setDestLatitude(endLocationLatitude);
				rideStatus.setDestLongitude(endLocationLongitude);
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

			List<DriverReceivedRides> driverReceivedRides = driverReceivedRidesRepository.findByRide(existingRide);

			if (driverReceivedRides.isEmpty()) {
				List<DriverAdditionalInfo> driverAdditionalInfos = driverAdditionalInfoRepository
						.findTopDriversInfoWithinRadius(existingRide.getSourceLongitude(),
								existingRide.getSourceLatitude(), 16000000,
								existingRide.getVehicleType().getTypeName());

				if (driverAdditionalInfos.size() != 0) {
					List<User> selectedDrivers = new ArrayList<>();

					for (DriverAdditionalInfo driverAdditionalInfo : driverAdditionalInfos) {
						selectedDrivers.add(driverAdditionalInfo.getDriver());

						DriverReceivedRides newDriverReceivedRides = new DriverReceivedRides();
						newDriverReceivedRides.setResponseStatus("No Response");
						newDriverReceivedRides.setReceivedAt(LocalDateTime.now());
						newDriverReceivedRides.setDriver(driverAdditionalInfo.getDriver());
						newDriverReceivedRides.setRide(existingRide);

						driverReceivedRidesRepository.save(newDriverReceivedRides);
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
					"The same ride request cannot be sent to near by drivers multiple times.");
			return response;

		}
		ServiceResponse<List<User>> response = new ServiceResponse<>(false, null,
				"Invalid ride id: " + rideId + ". Please, try again with a valid id!");
		return response;
	}

	@Override
	public ServiceResponse<Ride> completeRide(CompleteRideRecord completeRideRecord) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			if (currentLoggedInUser.getRole().getName().equalsIgnoreCase("Driver")) {
				Optional<Ride> rideOptional = rideRepository.findById(completeRideRecord.rideId());

				if (rideOptional.isPresent()) {
					Ride currentRide = rideOptional.get();

					if (currentRide.getDriver().getUserId() == currentLoggedInUser.getUserId()) {
						if (currentRide.getStatus().equalsIgnoreCase("In Progress")) {

							// Calculate the total final fare
							double totalFare = 0;

							Optional<RideStatus> rideStatusOptional = rideStatusRepository
									.findMostRecentByRideIdAndStatus(completeRideRecord.rideId(),
											"Destination Changed By Passenger");

							if (rideStatusOptional.isPresent()) {
								RideStatus rideStatus = rideStatusOptional.get();

								double estimatedRideFare = currentRide.getEstimatedFare();

								double prevSetRecentDistance = LocationUtils.calculateDistance(
										rideStatus.getSourceLatitude(), rideStatus.getSourceLongitude(),
										rideStatus.getDestLatitude(), rideStatus.getDestLongitude());

								double actualRecentDistance = LocationUtils.calculateDistance(
										rideStatus.getSourceLatitude(), rideStatus.getSourceLongitude(),
										completeRideRecord.destinationLatitude(),
										completeRideRecord.destinationLongitude());

								double prevPrice = prevSetRecentDistance
										* (currentRide.getVehicleType().getPricePerKm());

								double actualPrice = actualRecentDistance
										* (currentRide.getVehicleType().getPricePerKm());

								double overdue = userCreditsRepository.findByUserUserId(currentLoggedInUser.getUserId())
										.getOverDue();

								totalFare = (estimatedRideFare - prevPrice) + actualPrice + overdue;
							} else {
								double estimatedRideFare = currentRide.getEstimatedFare();

								double prevSetDistance = LocationUtils.calculateDistance(
										currentRide.getSourceLatitude(), currentRide.getSourceLongitude(),
										currentRide.getDestinationLatitude(), currentRide.getDestinationLongitude());

								double actualDistance = LocationUtils.calculateDistance(currentRide.getSourceLatitude(),
										currentRide.getSourceLongitude(), completeRideRecord.destinationLatitude(),
										completeRideRecord.destinationLongitude());

								double prevPrice = prevSetDistance * (currentRide.getVehicleType().getPricePerKm());

								double actualPrice = actualDistance * (currentRide.getVehicleType().getPricePerKm());

								double overdue = userCreditsRepository.findByUserUserId(currentLoggedInUser.getUserId())
										.getOverDue();

								totalFare = (estimatedRideFare - prevPrice) + actualPrice + overdue;

							}

							if (currentRide.getPaymentType().equalsIgnoreCase("Prepaid")) {

								UserCredits passengerCredits = userCreditsRepository
										.findByUserUserId(currentRide.getPassenger().getUserId());

								if (passengerCredits.getCurrentBalance() < totalFare) {
									ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
											"Insufficient balance in user credits. Please, add more amount or change the payment type to Postpaid.");
									return response;
								} else {
									currentRide.setRideEndTime(LocalDateTime.now());
									passengerCredits
											.setCurrentBalance(passengerCredits.getCurrentBalance() - totalFare);
									userCreditsRepository.save(passengerCredits);
									User driver = currentRide.getDriver();
									UserCredits driverCredits = userCreditsRepository
											.findByUserUserId(driver.getUserId());
									double grossAmount = totalFare * (40 / 100);
									double commission = grossAmount * (8 / 100);
									double finalPrice = grossAmount - commission;
									double driverTotalBalance = driverCredits.getCurrentBalance() + finalPrice;
									if (driverTotalBalance >= driverCredits.getOverDue()) {
										driverCredits
												.setCurrentBalance(driverTotalBalance - driverCredits.getOverDue());
										driverCredits.setOverDue((double) 0);
									} else {
										double driverFinalOverDue = driverCredits.getOverDue() - driverTotalBalance;
										driverCredits.setCurrentBalance((double) 0);
										driverCredits.setOverDue(driverFinalOverDue);
									}
									userCreditsRepository.save(driverCredits);
								}
								currentRide.setPaidAmount(totalFare);
								currentRide.setStatus("Payment Completed");

								// Database Triger to save the ride status details to the RideStatus table
								// simultaneously
								RideStatus rideStatus1 = new RideStatus();
								rideStatus1.setRideId(currentRide.getRideId());
								rideStatus1.setStatus("Completed");
								rideStatus1.setStatusUpdateTime(LocalDateTime.now());
								rideStatus1.setSourceName(currentRide.getSourceName());
								rideStatus1.setSourceLatitude(currentRide.getSourceLatitude());
								rideStatus1.setSourceLongitude(currentRide.getSourceLongitude());
								rideStatus1.setDestName(currentRide.getDestinationName());
								rideStatus1.setDestLatitude(currentRide.getDestinationLatitude());
								rideStatus1.setDestLongitude(currentRide.getDestinationLongitude());
								rideStatusRepository.save(rideStatus1);

								RideStatus rideStatus2 = new RideStatus();
								rideStatus2.setRideId(currentRide.getRideId());
								rideStatus2.setStatus("Payment Completed");
								rideStatus2.setStatusUpdateTime(LocalDateTime.now());
								rideStatus2.setSourceName(currentRide.getSourceName());
								rideStatus2.setSourceLatitude(currentRide.getSourceLatitude());
								rideStatus2.setSourceLongitude(currentRide.getSourceLongitude());
								rideStatus2.setDestName(currentRide.getDestinationName());
								rideStatus2.setDestLatitude(currentRide.getDestinationLatitude());
								rideStatus2.setDestLongitude(currentRide.getDestinationLongitude());
								rideStatusRepository.save(rideStatus2);

							} else {
								currentRide.setStatus("Completed");

								// Database Triger to save the ride status details to the RideStatus table
								// simultaneously
								RideStatus rideStatus = new RideStatus();
								rideStatus.setRideId(currentRide.getRideId());
								rideStatus.setStatus("Completed");
								rideStatus.setStatusUpdateTime(LocalDateTime.now());
								rideStatus.setSourceName(currentRide.getSourceName());
								rideStatus.setSourceLatitude(currentRide.getSourceLatitude());
								rideStatus.setSourceLongitude(currentRide.getSourceLongitude());
								rideStatus.setDestName(currentRide.getDestinationName());
								rideStatus.setDestLatitude(currentRide.getDestinationLatitude());
								rideStatus.setDestLongitude(currentRide.getDestinationLongitude());
								rideStatusRepository.save(rideStatus);
							}

							currentRide.setActualFare(totalFare);
							Ride updatedRide = rideRepository.save(currentRide);

							// Update driver's acceptance rate
							long acceptedRideCount = driverReceivedRidesRepository
									.countAcceptedRideRequestsByDriverId(currentLoggedInUser.getUserId());

							long totalReceivedRideCount = driverReceivedRidesRepository
									.countAllRideRequestsByDriverId(currentLoggedInUser.getUserId());

							double rideAcceptanceRate = (acceptedRideCount / totalReceivedRideCount) * 100;

							DriverAdditionalInfo driverAdditionalInfo = driverAdditionalInfoRepository
									.findByDriver(currentLoggedInUser);

							driverAdditionalInfo.setRideAcceptanceRate(rideAcceptanceRate);

							driverAdditionalInfoRepository.save(driverAdditionalInfo);

							ServiceResponse<Ride> response = new ServiceResponse<>(true, updatedRide,
									"Ride completed successfully. Please, process the payment");
							return response;
						}
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"This ride cannot be completed. The current ride status is: "
										+ currentRide.getStatus());
						return response;
					}
					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"Drivers can complete only the assigned rides.");
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
						"Invalid ride id: " + completeRideRecord.rideId());
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Only drivers can complete a ride.");
			return response;
		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<Ride> enRouteRide(Long rideId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			if (currentLoggedInUser.getRole().getName().equalsIgnoreCase("Driver")) {
				Optional<Ride> rideOptional = rideRepository.findById(rideId);

				if (rideOptional.isPresent()) {
					Ride currentRide = rideOptional.get();

					if (currentRide.getDriver().getUserId() == currentLoggedInUser.getUserId()) {
						if (currentRide.getStatus().equalsIgnoreCase("Accepted")) {

							currentRide.setStatus("En Route");
							Ride updatedRide = rideRepository.save(currentRide);

							// Database Triger to save the ride status details to the RideStatus table
							// simultaneously
							RideStatus rideStatus = new RideStatus();
							rideStatus.setRideId(updatedRide.getRideId());
							rideStatus.setStatus("En Routed");
							rideStatus.setStatusUpdateTime(LocalDateTime.now());
							rideStatus.setSourceName(updatedRide.getSourceName());
							rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
							rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
							rideStatus.setDestName(updatedRide.getDestinationName());
							rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
							rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
							rideStatusRepository.save(rideStatus);
						}
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"This ride cannot be en routed. The current ride status is: "
										+ currentRide.getStatus());
						return response;
					}
					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"Drivers can en route only the assigned rides.");
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid ride id: " + rideId);
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Only drivers can en route a ride.");
			return response;
		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}

	}

	@Override
	public ServiceResponse<Ride> makeRideStatusInProgress(Long rideId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			if (currentLoggedInUser.getRole().getName().equalsIgnoreCase("Driver")) {
				Optional<Ride> rideOptional = rideRepository.findById(rideId);

				if (rideOptional.isPresent()) {
					Ride currentRide = rideOptional.get();

					if (currentRide.getDriver().getUserId() == currentLoggedInUser.getUserId()) {
						if (currentRide.getStatus().equalsIgnoreCase("Picked Up")) {

							currentRide.setStatus("In Progress");
							Ride updatedRide = rideRepository.save(currentRide);

							// Database Triger to save the ride status details to the RideStatus table
							// simultaneously
							RideStatus rideStatus = new RideStatus();
							rideStatus.setRideId(updatedRide.getRideId());
							rideStatus.setStatus("In Progress");
							rideStatus.setStatusUpdateTime(LocalDateTime.now());
							rideStatus.setSourceName(updatedRide.getSourceName());
							rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
							rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
							rideStatus.setDestName(updatedRide.getDestinationName());
							rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
							rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
							rideStatusRepository.save(rideStatus);
						}
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"This ride cannot be made to In Progress. The current ride status is: "
										+ currentRide.getStatus());
						return response;
					}
					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"Drivers can progress only the assigned rides.");
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid ride id: " + rideId);
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Only drivers can progress a ride.");
			return response;
		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<Ride> changePaymentTypeAndMode(
			ChangePaymentTypeAndModeRecord changePaymentTypeAndModeRecord) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			Optional<Ride> rideOptional = rideRepository.findById(changePaymentTypeAndModeRecord.rideId());

			if (rideOptional.isPresent()) {
				Ride currentRide = rideOptional.get();

				if (currentRide.getPassenger().getUserId() == currentLoggedInUser.getUserId()) {
					if (currentRide.getStatus().equals("Canceled By Driver")
							|| currentRide.getStatus().equals("Canceled By User")
							|| currentRide.getStatus().equals("Driver Unavailable")
							|| currentRide.getStatus().equals("Payment Completed")
							|| currentRide.getStatus().equals("Feedback Received")) {
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"Payment type and mode cannot be changed. The ride's current status is: "
										+ currentRide.getStatus());
						return response;
					}

					if (changePaymentTypeAndModeRecord.paymentType().equalsIgnoreCase("Prepaid")
							&& !changePaymentTypeAndModeRecord.paymentMode().equalsIgnoreCase("Credits")) {
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"Payment type Prepaid can have only Credits as payment mode.");
						return response;
					}

					currentRide.setPaymentType(changePaymentTypeAndModeRecord.paymentType());
					currentRide.setPaymentMode(changePaymentTypeAndModeRecord.paymentMode());
					Ride updatedRide = rideRepository.save(currentRide);

					// Database Triger to save the ride status details to the RideStatus table
					// simultaneously
					RideStatus rideStatus = new RideStatus();
					rideStatus.setRideId(updatedRide.getRideId());
					rideStatus.setStatus("Payment Type And Mode Updated");
					rideStatus.setStatusUpdateTime(LocalDateTime.now());
					rideStatus.setSourceName(updatedRide.getSourceName());
					rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
					rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
					rideStatus.setDestName(updatedRide.getDestinationName());
					rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
					rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
					rideStatusRepository.save(rideStatus);

					ServiceResponse<Ride> response = new ServiceResponse<>(true, updatedRide,
							"Payment type and mode updated successfully.");
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
						"This ride is not of this passenger.");
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Invalid ride id: " + changePaymentTypeAndModeRecord.rideId());
			return response;

		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<Ride> cancelRideByDriver(Long rideId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			if (currentLoggedInUser.getRole().getName().equalsIgnoreCase("Driver")) {
				Optional<Ride> rideOptional = rideRepository.findById(rideId);

				if (rideOptional.isPresent()) {
					Ride currentRide = rideOptional.get();

					if (currentRide.getDriver().getUserId() == currentLoggedInUser.getUserId()) {
						if (currentRide.getStatus().equalsIgnoreCase("Accepted")
								|| currentRide.getStatus().equalsIgnoreCase("En Route")) {
							currentRide.setStatus("Canceled By Driver");
							Ride updatedRide = rideRepository.save(currentRide);

							DriverReceivedRides receivedRide = driverReceivedRidesRepository
									.findReceivedRidesByDriverAndRideIds(currentLoggedInUser.getUserId(), rideId);
							receivedRide.setResponseStatus("Canceled");
							driverReceivedRidesRepository.save(receivedRide);

							// Database Triger to save the ride status details to the RideStatus table
							// simultaneously
							RideStatus rideStatus = new RideStatus();
							rideStatus.setRideId(updatedRide.getRideId());
							rideStatus.setStatus("Canceled By Driver");
							rideStatus.setStatusUpdateTime(LocalDateTime.now());
							rideStatus.setSourceName(updatedRide.getSourceName());
							rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
							rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
							rideStatus.setDestName(updatedRide.getDestinationName());
							rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
							rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
							rideStatusRepository.save(rideStatus);

							ServiceResponse<Ride> response = new ServiceResponse<>(true, updatedRide,
									"The ride has been canceled by the driver.");
							return response;
						}
						ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
								"The ride cannot be canceled by the driver. The ride status is: "
										+ currentRide.getStatus());
						return response;
					}

					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"The current driver is not assigned with the provided ride.");
					return response;
				}

				ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid ride id: " + rideId);
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Current logged in user is not a driver.");
			return response;

		} else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<Ride> cancelRideByPassenger(Long rideId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User currentLoggedInUser = userOptional.get();

			Optional<Ride> rideOptional = rideRepository.findById(rideId);

			if (rideOptional.isPresent()) {
				Ride currentRide = rideOptional.get();

				if (currentRide.getPassenger().getUserId() == currentLoggedInUser.getUserId()) {
					if (currentRide.getStatus().equals("Booked") || currentRide.getStatus().equals("Accepted")
							|| currentRide.getStatus().equals("En Route")) {
						if (currentRide.getStatus().equals("Accepted") || currentRide.getStatus().equals("En Route")) {

							LocalDateTime acceptedTime = rideStatusRepository.findAcceptedTimeByRideId(rideId);

							Duration duration = Duration.between(acceptedTime, LocalDateTime.now());

							long minutes = duration.toMinutes();

							if (minutes > 5) {
								if (currentRide.getPaymentType().equalsIgnoreCase("Prepaid")) {
									double userCreditBalance = userCreditsRepository
											.findByUserUserId(currentLoggedInUser.getUserId()).getCurrentBalance();

									UserCredits userCredit = userCreditsRepository
											.findByUserUserId(currentLoggedInUser.getUserId());
									if (userCreditBalance >= 25) {
										userCredit.setCurrentBalance(userCredit.getCurrentBalance() - 25);
										userCreditsRepository.save(userCredit);
									} else {
										// Insufficient balance
										userCredit.setOverDue(userCredit.getOverDue() + 25);
										userCreditsRepository.save(userCredit);
									}
								} else {
									UserCredits userCredit = userCreditsRepository
											.findByUserUserId(currentLoggedInUser.getUserId());
									userCredit.setOverDue(userCredit.getOverDue() + 25);
									userCreditsRepository.save(userCredit);
								}
							}

						}
						currentRide.setStatus("Canceled By Passenger");
						Ride updatedRide = rideRepository.save(currentRide);

						// Database Triger to save the ride status details to the RideStatus table
						// simultaneously
						RideStatus rideStatus = new RideStatus();
						rideStatus.setRideId(updatedRide.getRideId());
						rideStatus.setStatus("Canceled By Passenger");
						rideStatus.setStatusUpdateTime(LocalDateTime.now());
						rideStatus.setSourceName(updatedRide.getSourceName());
						rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
						rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
						rideStatus.setDestName(updatedRide.getDestinationName());
						rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
						rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
						rideStatusRepository.save(rideStatus);

						// Update driver's acceptance rate
						long acceptedRideCount = driverReceivedRidesRepository
								.countAcceptedRideRequestsByDriverId(updatedRide.getDriver().getUserId());

						long totalReceivedRideCount = driverReceivedRidesRepository
								.countAllRideRequestsByDriverId(updatedRide.getDriver().getUserId());

						double rideAcceptanceRate = (acceptedRideCount / totalReceivedRideCount) * 100;

						DriverAdditionalInfo driverAdditionalInfo = driverAdditionalInfoRepository
								.findByDriver(updatedRide.getDriver());

						driverAdditionalInfo.setRideAcceptanceRate(rideAcceptanceRate);

						driverAdditionalInfoRepository.save(driverAdditionalInfo);

						ServiceResponse<Ride> response = new ServiceResponse<>(true, updatedRide,
								"The ride has been canceled by the passenger successfully.");
						return response;
					}

					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"The ride cannot be canceled. The ride's current status is: " + currentRide.getStatus());
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
						"The current user is not the passenger for this ride.");
				return response;
			}

			ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid ride id: " + rideId);
			return response;
		}

		else {
			// No user is authenticated
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

	@Override
	public ServiceResponse<Ride> makePaymentForRide(MakePaymentRecord makePaymentRecord) {
		Optional<Ride> rideOptional = rideRepository.findById(makePaymentRecord.rideId());

		if (rideOptional.isPresent()) {
			Ride currentRide = rideOptional.get();

			if (currentRide.getStatus().equalsIgnoreCase("Completed")) {
				if (currentRide.getPaymentType().equalsIgnoreCase("Prepaid")) {
					ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
							"All Prepaid rides are done with payment when the get completed.");
					return response;
				}

				User driver = currentRide.getDriver();
				UserCredits driverCredits = userCreditsRepository.findByUserUserId(driver.getUserId());

				double tip = 0;
				if (makePaymentRecord.amount() > currentRide.getActualFare()) {
					tip = makePaymentRecord.amount() - currentRide.getActualFare();
				}
				double grossAmount = currentRide.getActualFare() * (40 / 100);
				double commission = grossAmount * (8 / 100);
				double finalPrice = grossAmount - commission;
				double driverTotalBalance = driverCredits.getCurrentBalance() + finalPrice + tip;
				if (driverTotalBalance >= driverCredits.getOverDue()) {
					driverCredits.setCurrentBalance(driverTotalBalance - driverCredits.getOverDue());
					driverCredits.setOverDue((double) 0);
				} else {
					double driverFinalOverDue = driverCredits.getOverDue() - driverTotalBalance;
					driverCredits.setCurrentBalance((double) 0);
					driverCredits.setOverDue(driverFinalOverDue);
				}
				userCreditsRepository.save(driverCredits);

				currentRide.setPaidAmount(makePaymentRecord.amount());
				currentRide.setStatus("Payment Completed");
				Ride updatedRide = rideRepository.save(currentRide);

				// Database Triger to save the ride status details to the RideStatus table
				// simultaneously
				RideStatus rideStatus = new RideStatus();
				rideStatus.setRideId(updatedRide.getRideId());
				rideStatus.setStatus("Payment Completed");
				rideStatus.setStatusUpdateTime(LocalDateTime.now());
				rideStatus.setSourceName(updatedRide.getSourceName());
				rideStatus.setSourceLatitude(updatedRide.getSourceLatitude());
				rideStatus.setSourceLongitude(updatedRide.getSourceLongitude());
				rideStatus.setDestName(updatedRide.getDestinationName());
				rideStatus.setDestLatitude(updatedRide.getDestinationLatitude());
				rideStatus.setDestLongitude(updatedRide.getDestinationLongitude());
				rideStatusRepository.save(rideStatus);

				ServiceResponse<Ride> response = new ServiceResponse<>(true, updatedRide,
						"Payment made for the ride successfully.");
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
					"You can make payment once the ride is completed. Now, the ride status is: "
							+ currentRide.getStatus());
			return response;
		}
		ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
				"Invalid ride Id: " + makePaymentRecord.rideId());
		return response;
	}

	@Override
	public ServiceResponse<RatingAndFeedback> giveRatingFeedback(RatingFeedbackRecord ratingFeedbackRecord) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> userOptional = userRepository.findByEmail(username);
			User giver = userOptional.get();

			Optional<Ride> rideOptional = rideRepository.findById(ratingFeedbackRecord.rideId());

			if (rideOptional.isPresent()) {
				Ride currentRide = rideOptional.get();

				if (currentRide.getDriver().getUserId() == giver.getUserId()
						|| currentRide.getPassenger().getUserId() == giver.getUserId()) {
					if (currentRide.getStatus().equalsIgnoreCase("Payment Completed")) {
						RatingAndFeedback updatedRatingAndFeedback = new RatingAndFeedback();

						if (currentRide.getDriver().getUserId() == giver.getUserId()) {
							User passenger = currentRide.getPassenger();

							RatingAndFeedback ratingAndFeedback = new RatingAndFeedback();
							ratingAndFeedback.setRating(ratingFeedbackRecord.rating());
							ratingAndFeedback.setFeedback(ratingFeedbackRecord.feedback());
							ratingAndFeedback.setRide(currentRide);
							ratingAndFeedback.setGiver(giver);
							ratingAndFeedback.setReceiver(passenger);

							updatedRatingAndFeedback = ratingAndFeedBackRepository.save(ratingAndFeedback);

							double passengerAverageRating = ratingAndFeedBackRepository
									.findAverageRatingByReceiverId(passenger.getUserId());
							passenger.setAverageRatingAsPassenger(passengerAverageRating);
							userRepository.save(passenger);

							// Database Triger to save the ride status details to the RideStatus table
							// simultaneously
							RideStatus rideStatus = new RideStatus();
							rideStatus.setRideId(currentRide.getRideId());
							rideStatus.setStatus("Driver Feedback Received");
							rideStatus.setStatusUpdateTime(LocalDateTime.now());
							rideStatus.setSourceName(currentRide.getSourceName());
							rideStatus.setSourceLatitude(currentRide.getSourceLatitude());
							rideStatus.setSourceLongitude(currentRide.getSourceLongitude());
							rideStatus.setDestName(currentRide.getDestinationName());
							rideStatus.setDestLatitude(currentRide.getDestinationLatitude());
							rideStatus.setDestLongitude(currentRide.getDestinationLongitude());
							rideStatusRepository.save(rideStatus);
						} else {
							User driver = currentRide.getDriver();

							RatingAndFeedback ratingAndFeedback = new RatingAndFeedback();
							ratingAndFeedback.setRating(ratingFeedbackRecord.rating());
							ratingAndFeedback.setFeedback(ratingFeedbackRecord.feedback());
							ratingAndFeedback.setRide(currentRide);
							ratingAndFeedback.setGiver(giver);
							ratingAndFeedback.setReceiver(driver);

							updatedRatingAndFeedback = ratingAndFeedBackRepository.save(ratingAndFeedback);

							double driverAverageRating = ratingAndFeedBackRepository
									.findAverageRatingByReceiverId(driver.getUserId());
							DriverAdditionalInfo driverAdditionalInfo = driverAdditionalInfoRepository
									.findByDriver(driver);
							driverAdditionalInfo.setAverageRating(driverAverageRating);
							driverAdditionalInfoRepository.save(driverAdditionalInfo);

							// Database Triger to save the ride status details to the RideStatus table
							// simultaneously
							RideStatus rideStatus = new RideStatus();
							rideStatus.setRideId(currentRide.getRideId());
							rideStatus.setStatus("Passenger Feedback Received");
							rideStatus.setStatusUpdateTime(LocalDateTime.now());
							rideStatus.setSourceName(currentRide.getSourceName());
							rideStatus.setSourceLatitude(currentRide.getSourceLatitude());
							rideStatus.setSourceLongitude(currentRide.getSourceLongitude());
							rideStatus.setDestName(currentRide.getDestinationName());
							rideStatus.setDestLatitude(currentRide.getDestinationLatitude());
							rideStatus.setDestLongitude(currentRide.getDestinationLongitude());
							rideStatusRepository.save(rideStatus);
						}

						ServiceResponse<RatingAndFeedback> response = new ServiceResponse<>(true,
								updatedRatingAndFeedback, "Rating and feedback for this ride received successfully.");
						return response;
					}
					ServiceResponse<RatingAndFeedback> response = new ServiceResponse<>(false, null,
							"Rating and feedback for this ride cannot be given, now. The status of the ride is: "
									+ currentRide.getStatus());
					return response;
				}

				ServiceResponse<RatingAndFeedback> response = new ServiceResponse<>(false, null,
						"The current user is neither the passenger not the driver for this ride.");
				return response;
			}
			ServiceResponse<RatingAndFeedback> response = new ServiceResponse<>(false, null,
					"Invalid ride id: " + ratingFeedbackRecord.rideId());
			return response;
		}

		else {
			// No user is authenticated
			ServiceResponse<RatingAndFeedback> response = new ServiceResponse<>(false, null,
					"Currently no user is authenticated. Please, login first!");
			return response;
		}
	}

}
