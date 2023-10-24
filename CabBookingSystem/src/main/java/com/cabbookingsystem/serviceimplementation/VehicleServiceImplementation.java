package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.Vehicle;
import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleRecord;
import com.cabbookingsystem.repository.VehicleModelRepository;
import com.cabbookingsystem.repository.VehicleRepository;
import com.cabbookingsystem.service.VehicleService;

@Service
public class VehicleServiceImplementation implements VehicleService {
	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Override
	public ServiceResponse<Vehicle> addVehicle(AddVehicleRecord addVehicleRecord) {
		Optional<Vehicle> vehicleOptional = vehicleRepository
				.findByRegistrationNumber(addVehicleRecord.registrationNumber());

		if (vehicleOptional.isEmpty()) {
			Vehicle newVehicle = new Vehicle();
			newVehicle.setRegistrationNumber(addVehicleRecord.registrationNumber());
			newVehicle.setCapacity(addVehicleRecord.capacity());
			newVehicle.setActive("Inactive");
			newVehicle.setAvailable("Available");

			Vehicle addedVehicle = vehicleRepository.save(newVehicle);

			ServiceResponse<Vehicle> response = new ServiceResponse<>(true, addedVehicle,
					"Vehicle added successfully!");
			return response;
		}
		ServiceResponse<Vehicle> response = new ServiceResponse<>(false, null, "Vehicle with registration number: "
				+ addVehicleRecord.registrationNumber() + " already exists and can't be added again.");
		return response;
	}

	@Override
	public ServiceResponse<Vehicle> assignVehicleModelToVehicle(Long vehicleModelId, Long vehicleId) {
		Optional<VehicleModel> vehicleModelOptional = vehicleModelRepository.findById(vehicleModelId);

		if (vehicleModelOptional.isPresent()) {
			VehicleModel existingVehicleModel = vehicleModelOptional.get();

			Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);

			if (vehicleOptional.isPresent()) {
				Vehicle existingVehicle = vehicleOptional.get();

				existingVehicle.setVehicleModel(existingVehicleModel);

				Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

				ServiceResponse<Vehicle> response = new ServiceResponse<>(true, updatedVehicle,
						"Vehicle model successfully assigned to vehicle!");
				return response;
			}
			ServiceResponse<Vehicle> response = new ServiceResponse<>(false, null,
					"Invalid Vehicle id: " + vehicleId + ". Please, try again with a valid vehicle id.");
			return response;
		}

		ServiceResponse<Vehicle> response = new ServiceResponse<>(false, null,
				"Invalid Vehicle model id: " + vehicleModelId + ". Please, try again with a valid model id.");
		return response;
	}
}
