package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.CreateUserRecord;

public interface UserService {
	public ServiceResponse<User> createUser(CreateUserRecord createUserRecord);
}
