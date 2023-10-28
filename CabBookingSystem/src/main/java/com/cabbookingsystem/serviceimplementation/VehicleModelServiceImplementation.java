package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.VehicleModel;
import com.cabbookingsystem.entity.VehicleType;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddVehicleModelRecord;
import com.cabbookingsystem.record.AssignVehicleTypeToVehicleModelRecord;
import com.cabbookingsystem.repository.VehicleModelRepository;
import com.cabbookingsystem.repository.VehicleTypeRepository;
import com.cabbookingsystem.service.VehicleModelService;

@Service
public class VehicleModelServiceImplementation implements VehicleModelService {

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

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

	@Override
	public ServiceResponse<VehicleModel> assignVehicleTypeToVehicleModel(
			AssignVehicleTypeToVehicleModelRecord assignVehicleTypeToVehicleModelRecord) {
		Optional<VehicleType> vehicleTypeOptional = vehicleTypeRepository
				.findByTypeName(assignVehicleTypeToVehicleModelRecord.typeName().toLowerCase());

		if (vehicleTypeOptional.isPresent()) {
			VehicleType vehicleType = vehicleTypeOptional.get();

			Optional<VehicleModel> vehicleModelOptional = vehicleModelRepository
					.findByModelName(assignVehicleTypeToVehicleModelRecord.modelName().toLowerCase());

			if (vehicleModelOptional.isPresent()) {
				VehicleModel vehicleModel = vehicleModelOptional.get();

				vehicleModel.setVehicleType(vehicleType);

				VehicleModel updatedVehicleModel = vehicleModelRepository.save(vehicleModel);

				ServiceResponse<VehicleModel> response = new ServiceResponse<>(true, updatedVehicleModel,
						"Vehicle type assigned to vehicle model successfully!");
				return response;
			}

			ServiceResponse<VehicleModel> response = new ServiceResponse<>(false, null,
					"Vehicle model with name: " + assignVehicleTypeToVehicleModelRecord.modelName() + " not found.");
			return response;
		}

		ServiceResponse<VehicleModel> response = new ServiceResponse<>(false, null,
				"Vehicle type with name: " + assignVehicleTypeToVehicleModelRecord.typeName() + " not found.");
		return response;
	}

}
