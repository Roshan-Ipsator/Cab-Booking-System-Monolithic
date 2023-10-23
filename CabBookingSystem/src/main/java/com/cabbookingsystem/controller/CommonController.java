package com.cabbookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabbookingsystem.payload.ApiResponse;
import com.cabbookingsystem.record.SetProfileDetailsRecord;
import com.cabbookingsystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/common")
//@PreAuthorize("hasAnyRole('USER_ALL_ACCESS', 'USER_DEFAULT_ACCESS', 'ADMIN_ALL_ACCESS','ADMIN_DEFAULT_ACCESS')")
public class CommonController {
	@Autowired
	private UserService userService;

	/**
	 * API end point for updating a user's details
	 * 
	 * @return ResponseEntity<ApiResponse> where ApiResponse contains the updated
	 *         user details as its data
	 */
	@PutMapping("/update-user")
//	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE', 'USER_UPDATE')")
	public ResponseEntity<ApiResponse> setProfileDetails(
			@Valid @RequestBody SetProfileDetailsRecord setProfileDetailsRecord) {
		ResponseEntity<ApiResponse> setProfileDetailsResponse = userService.setProfileDetails(setProfileDetailsRecord)
				.finalResponse();
		return setProfileDetailsResponse;
	}
}
