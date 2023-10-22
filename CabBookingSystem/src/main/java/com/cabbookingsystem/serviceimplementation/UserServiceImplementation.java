package com.cabbookingsystem.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.CreateUserRecord;
import com.cabbookingsystem.repository.UserRepository;
import com.cabbookingsystem.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public ServiceResponse<User> createUser(CreateUserRecord createUserRecord) {
		Optional<User> userOptionalEmail = userRepository.findByEmail(createUserRecord.email());

		if (userOptionalEmail.isPresent()) {
			ServiceResponse<User> response = new ServiceResponse<>(false, null,
					"Provided email id already exists. Please, try with a new one.");
			return response;
		} else {
			Optional<User> userOptionalPhone = userRepository.findByPhone(createUserRecord.phone());
			if (userOptionalPhone.isPresent()) {
				ServiceResponse<User> response = new ServiceResponse<>(false, null,
						"Provided phone number already exists. Please, try with a new one.");
				return response;
			}
			User newUser = new User(createUserRecord.firstName(), createUserRecord.lastName(), createUserRecord.email(),
					createUserRecord.phone());

			User createdUser = userRepository.save(newUser);

			ServiceResponse<User> response = new ServiceResponse<>(true, createdUser, "New user successfully created.");
			return response;

		}
	}

}
