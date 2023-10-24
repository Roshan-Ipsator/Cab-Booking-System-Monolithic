package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.repository.VehicleModelRepository;
import com.cabbookingsystem.service.VehicleModelService;

@Service
public class VehicleModelServiceImplementation implements VehicleModelService {

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Override
	public ServiceResponse<VehicleModel> addVehicleModel(VehicleModel vehicleModel) {
		Optional<VehicleModel> vehicleModelOptional = vehicleModelRepository
				.findByModelName(vehicleModel.getModelName());

		if (vehicleModelOptional.isEmpty()) {
			VehicleModel newVehicleModel = new VehicleModel();
			newVehicleModel.setModelName(vehicleModel.getModelName());
			newVehicleModel.setBrand(vehicleModel.getBrand());
			newVehicleModel.setModelDescription(vehicleModel.getModelDescription());

			VehicleModel addedVehicleModel = vehicleModelRepository.save(newVehicleModel);

			ServiceResponse<VehicleModel> response = new ServiceResponse<>(true, addedVehicleModel,
					"New Vehicle model added successfully!");
			return response;
		}
		ServiceResponse<VehicleModel> response = new ServiceResponse<>(false, null, "Vehicle model with name: "
				+ vehicleModel.getModelName() + " already exists and can't be added again.");
		return response;
	}

}
