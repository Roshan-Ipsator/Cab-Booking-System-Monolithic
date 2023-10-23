package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.service.UserService;

@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasAnyRole('ADMIN_ALL_ACCESS','ADMIN_DEFAULT_ACCESS', 'ADMIN_READ_ACCESS')")
public class AdminController {
	@Autowired
	private UserService userService;

	/**
	 * API end point for Getting All Users
	 * 
	 * @return List of users
	 */
	@GetMapping("/get-all-users")
//	@PreAuthorize("hasAuthority('ADMIN_READ')")
	public ResponseEntity<ApiResponse> getAllUsers() {
		ResponseEntity<ApiResponse> getAllUsersResponseResponse = userService.getAllUsers().finalResponse();
		return getAllUsersResponseResponse;
	}
}
