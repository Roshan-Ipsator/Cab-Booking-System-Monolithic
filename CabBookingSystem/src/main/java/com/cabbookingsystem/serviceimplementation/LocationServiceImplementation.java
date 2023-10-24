package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.Location;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.CreateLocationRecord;
import com.cabbookingsystem.repository.LocationRepository;
import com.cabbookingsystem.service.LocationService;

@Service
public class LocationServiceImplementation implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public ServiceResponse<Location> createLocation(CreateLocationRecord createLocationRecord) {
		Optional<Location> locationOptional = locationRepository.findByName(createLocationRecord.name());

		if (locationOptional.isEmpty()) {
			Location newLocation = new Location();
			newLocation.setName(createLocationRecord.name());
			newLocation.setLatitude(1.0 + (100.0 - 1.0) * Math.random());
			newLocation.setLongitude(1.0 + (100.0 - 1.0) * Math.random());

			Location createdLocation = locationRepository.save(newLocation);

			ServiceResponse<Location> response = new ServiceResponse<>(true, createdLocation,
					"Location created successfully!");
			return response;
		}
		ServiceResponse<Location> response = new ServiceResponse<>(false, null, "Provided location name : "
				+ createLocationRecord.name() + " already exists. It can't be added again.");
		return response;
	}

}
