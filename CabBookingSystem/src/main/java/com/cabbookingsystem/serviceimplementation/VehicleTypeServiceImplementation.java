package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.repository.VehicleTypeRepository;
import com.cabbookingsystem.service.VehicleTypeService;

@Service
public class VehicleTypeServiceImplementation implements VehicleTypeService {

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	@Override
	public ServiceResponse<VehicleType> addVehicleType(VehicleType vehicleType) {
		Optional<VehicleType> vehicleTypeOptional = vehicleTypeRepository.findByTypeName(vehicleType.getTypeName());

		if (vehicleTypeOptional.isEmpty()) {
			VehicleType newVehicleType = new VehicleType();
			newVehicleType.setTypeName(vehicleType.getTypeName());
			newVehicleType.setSeatingCapacity(vehicleType.getSeatingCapacity());
			newVehicleType.setPricePerKm(vehicleType.getPricePerKm());
			newVehicleType.setTypeDescription(vehicleType.getTypeDescription());

			VehicleType addedVehicleType = vehicleTypeRepository.save(newVehicleType);

			ServiceResponse<VehicleType> response = new ServiceResponse<>(true, addedVehicleType,
					"New Vehicle model added successfully!");
			return response;
		}
		ServiceResponse<VehicleType> response = new ServiceResponse<>(false, null,
				"Vehicle type with name: " + vehicleType.getTypeName() + " already exists and can't be added again.");
		return response;
	}

}
