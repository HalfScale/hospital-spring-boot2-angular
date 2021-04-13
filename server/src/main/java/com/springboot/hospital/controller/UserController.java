package com.springboot.hospital.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.model.dto.ProfileDTO;
import com.springboot.hospital.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<ProfileDTO> getLoggedUser() {
		return ResponseEntity.ok(userService.getUserProfile());
	}
	
	@PutMapping
	public ResponseEntity<?> updateProfile(@RequestParam("updateData") String profileDto,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		LOGGER.info("updateProfile => data: [{}], file: [{}]", profileDto, file);
		userService.updateProfile(profileDto, file);
		return ResponseEntity.ok().build();
	}
}
