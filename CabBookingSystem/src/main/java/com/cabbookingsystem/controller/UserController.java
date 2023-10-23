package com.cabbookingsystem.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	/**
	 * API end point for getting username of current authenticated user
	 * 
	 * @param principal a Principal obejct
	 * @return ResponseEntity object
	 */
	@GetMapping
	public ResponseEntity<String> getUserName(Principal principal) {
		return new ResponseEntity<>("Currently authenticated user's username: " + principal.getName(), HttpStatus.OK);
	}

}