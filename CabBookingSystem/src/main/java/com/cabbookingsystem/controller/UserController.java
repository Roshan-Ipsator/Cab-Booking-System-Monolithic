//package com.cabbookingsystem.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cabbookingsystem.payload.ApiResponse;
//import com.cabbookingsystem.record.CreateUserRecord;
//import com.cabbookingsystem.service.UserService;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//	@Autowired
//	private UserService userService;
//
//	@PostMapping
//	public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRecord createUserRecord) {
//		return userService.createUser(createUserRecord).finalResponse();
//	}
//
//}
