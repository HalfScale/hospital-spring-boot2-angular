package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.dto.ProfileDTO;
import com.springboot.hospital.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PutMapping
	public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileDto) {
		userService.updateProfile(profileDto);
		return ResponseEntity.ok().build();
	}
}
