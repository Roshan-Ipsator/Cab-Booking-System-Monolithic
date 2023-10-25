package com.cabbookingsystem.serviceimplementation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.Location;
import com.cabbookingsystem.entity.Ride;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.entity.Vehicle;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.BookRideRecord;
import com.cabbookingsystem.repository.LocationRepository;
import com.cabbookingsystem.repository.RideRepository;
import com.cabbookingsystem.repository.UserRepository;
import com.cabbookingsystem.service.RideService;

@Service
public class RideServiceImplementation implements RideService {

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public ServiceResponse<Ride> bookRide(BookRideRecord bookRideRecord) {
		Optional<User> userOptional = userRepository.findById(bookRideRecord.passengerId());

		if (userOptional.isPresent()) {
			User passenger = userOptional.get();

			Optional<Location> sourceOptional = locationRepository.findById(bookRideRecord.sourceId());

			if (sourceOptional.isPresent()) {
				Location source = sourceOptional.get();

				Optional<Location> destinationOptional = locationRepository.findById(bookRideRecord.destinationId());

				if (destinationOptional.isPresent()) {
					Location destination = destinationOptional.get();

					Ride newRide = new Ride();
					newRide.setPassenger(passenger);
					newRide.setStartLocation(source);
					newRide.setEndLocation(destination);
					newRide.setFare((int) (Math.random() * 100));
					newRide.setStatus("Pending");
					newRide.setRideDateTime(LocalDateTime.now());

					Ride bookedRide = rideRepository.save(newRide);

					ServiceResponse<Ride> response = new ServiceResponse<>(true, bookedRide,
							"Ride booked successfully!");
					return response;
				}
				ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid destination id: "
						+ bookRideRecord.destinationId() + ". Please, try again with a valid location id!");
				return response;
			}
			ServiceResponse<Ride> response = new ServiceResponse<>(false, null, "Invalid source id: "
					+ bookRideRecord.sourceId() + ". Please, try again with a valid location id!");
			return response;
		}

		ServiceResponse<Ride> response = new ServiceResponse<>(false, null,
				"Invalid passenger id: " + bookRideRecord.passengerId() + ". Please, try again with a valid user id!");
		return response;
	}

}
