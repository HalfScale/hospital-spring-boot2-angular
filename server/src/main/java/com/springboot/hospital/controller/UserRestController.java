package com.springboot.hospital.controller;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.Response;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.handler.OnRegistrationCompleteEvent;
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
		
		logger.info("Catched token: {}", token);
		Optional<User> user = userService.getVerificationToken(token);
		
		
		if (!user.isPresent() || user.get().isConfirmed()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		logger.info("User: {}, Successful verification", user.get().getUserDetail().getFirstName());
		
		user.get().setConfirmed(true);
		userService.save(user.get());
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/auth/login")
	public AuthenticationResponse login(@Validated(HospitalValidationSequence.class) @RequestBody LoginRequest loginRequest) {
		return userService.login(loginRequest);
	} 
	
	@PutMapping("/users/edit")
	public Response updateUser(@RequestBody User user) throws Exception{
		User persistedUser = userService.update(user);
		return Utils.<User>generateResponse(0, "User update successful", persistedUser);
	}
	
	@GetMapping("/users")
	public Response getUsers() throws Exception{
		List<User> users = userService.findAll();
		return Utils.<List<User>>generateResponse(0, "Query successful!", users);
	}
	
	@GetMapping("/users/{id}")
	public Response getUser(@PathVariable int id) throws Exception{
		Optional<User> user = userService.findById(id);
		
		if (!user.isPresent()) {
			throw new Exception("User is not present");
		}
		
		return Utils.<User>generateResponse(0, "Query successful!", user.get());
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
