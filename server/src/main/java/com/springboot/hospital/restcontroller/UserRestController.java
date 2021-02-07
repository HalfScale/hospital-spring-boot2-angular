package com.springboot.hospital.restcontroller;


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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
		
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, request.getLocale(), appUrl));
		
		return Utils.<User>generateResponse(0, "Registration successful!", savedUser);
	}
	
	@GetMapping("/registration/confirm/{token}/**")
	public void confirmUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String message = "Unsuccessful confirmation";
		
		String requestURL = request.getRequestURL().toString();
		String token = requestURL.split("/confirm/")[1];
		
		logger.info("Catched token: {}", token);
		User user = userService.getVerificationToken(token);
		
		
		if (user == null) {
			response.sendRedirect("http://localhost:4200/home");
		}
		
		logger.info("User: {}, Successful verification", user.getUserDetail().getFirstName());
		
		user.setConfirmed(true);
		userService.save(user);
		message = "Successful confirmation of email";
		
		response.sendRedirect("http://localhost:4200/home");
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
}
