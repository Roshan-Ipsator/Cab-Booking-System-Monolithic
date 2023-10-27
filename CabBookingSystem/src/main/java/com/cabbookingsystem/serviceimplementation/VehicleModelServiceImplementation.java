package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleModelRecord;
import com.cabbookingsystem.repository.VehicleModelRepository;
import com.cabbookingsystem.service.VehicleModelService;

@Service
public class VehicleModelServiceImplementation implements VehicleModelService {

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Override
	public ServiceResponse<VehicleModel> addVehicleModel(AddVehicleModelRecord addVehicleModelRecord) {
		Optional<VehicleModel> vehicleModelOptional = vehicleModelRepository
				.findByModelName(addVehicleModelRecord.modelName());

		if (vehicleModelOptional.isEmpty()) {
			VehicleModel newVehicleModel = new VehicleModel();
			newVehicleModel.setModelName(addVehicleModelRecord.modelName());
			newVehicleModel.setBrand(addVehicleModelRecord.brand());
			newVehicleModel.setModelDescription(addVehicleModelRecord.modelDescription());

			VehicleModel addedVehicleModel = vehicleModelRepository.save(newVehicleModel);

			ServiceResponse<VehicleModel> response = new ServiceResponse<>(true, addedVehicleModel,
					"New Vehicle model added successfully!");
			return response;
		}
		ServiceResponse<VehicleModel> response = new ServiceResponse<>(false, null, "Vehicle model with name: "
				+ addVehicleModelRecord.modelName() + " already exists and can't be added again.");
		return response;
	}

}
