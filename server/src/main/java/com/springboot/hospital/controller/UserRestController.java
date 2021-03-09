package com.springboot.hospital.controller;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.dto.RefreshTokenRequest;
import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.Response;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.handler.OnRegistrationCompleteEvent;
import com.springboot.hospital.service.RefreshTokenService;
import com.springboot.hospital.service.UserService;
import com.springboot.hospital.util.Utils;
import com.springboot.hospital.validator.HospitalValidationSequence;


@RestController
public class UserRestController {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/processRegistration")
	public Response registerUser(
			@RequestBody RegistrationForm form, 
			BindingResult result,
			HttpServletRequest request) throws MethodArgumentNotValidException, IOException {
		
		logger.info("form request: {}", form.toString());
		
		Set<ConstraintViolation<RegistrationForm>> violations  = validator.validate(form, HospitalValidationSequence.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		
		User savedUser = userService.registerNewUserAccount(form);
		
		String appUrl = getAppUrl(request);
		logger.info("server name {}", request.getServerName() + request.getServerPort());
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, request.getLocale(), appUrl));
		
		return Utils.<User>generateResponse(0, "Registration successful!", savedUser);
	}
	
	@GetMapping("/registration/confirm/{token}/**")
	public ResponseEntity confirmUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String requestURL = request.getRequestURL().toString();
		String token = requestURL.split("/confirm/")[1];
		
		logger.info("Token request: {}", token);
		Optional<User> queriedUser = userService.getVerificationToken(token);
		User user = queriedUser.isPresent() ? queriedUser.get() : null;
		
		
		if (user == null || user.isConfirmed() || user.isDeleted() || !userService.isValidToken(user)) {
			logger.info("Invalid token");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		logger.info("User: {}, Successful verification", user.getUserDetail().getFirstName());
		
		user.setConfirmed(true);
		userService.save(user);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/auth/login")
	public AuthenticationResponse login(@Validated(HospitalValidationSequence.class) @RequestBody LoginRequest loginRequest) {
		return userService.login(loginRequest);
	}
	
	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return userService.refreshToken(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!");
	}
	
	private String getAppUrl(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme() + "://");
		sb.append(request.getServerName() + ":");
		sb.append(request.getServerPort());
		sb.append(request.getContextPath());
		return sb.toString();
	}
}
