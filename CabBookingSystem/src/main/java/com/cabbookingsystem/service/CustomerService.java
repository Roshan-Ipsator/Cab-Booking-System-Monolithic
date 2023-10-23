package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.CreateUserRecord;

public interface CustomerService {
	public ServiceResponse<User> createCustomer(CreateUserRecord createUserRecord);
}
