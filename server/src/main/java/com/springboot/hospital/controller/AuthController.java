package com.springboot.hospital.controller;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.dto.PasswordResetNotificationRequest;
import com.springboot.hospital.dto.PasswordResetRequest;
import com.springboot.hospital.dto.RefreshTokenRequest;
import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.Response;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.service.RefreshTokenService;
import com.springboot.hospital.service.UserService;
import com.springboot.hospital.util.Utils;
import com.springboot.hospital.validator.HospitalValidationSequence;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${client.app.url}")
	private String clientAppUrl;
	
	@PostMapping("/registration")
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
		
		return Utils.<User>generateResponse(0, "Registration successful!", savedUser);
	}
	
	@GetMapping("/registration/confirm/{token}/**")
	public void confirmUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String requestURL = request.getRequestURL().toString();
		String token = requestURL.split("/confirm/")[1];
		
		logger.info("Token request: {}", token);
		Optional<User> queriedUser = userService.getVerificationToken(token);
		User user = queriedUser.isPresent() ? queriedUser.get() : null;
		
		
		if (user == null || user.isConfirmed() || user.isDeleted() || !userService.isValidToken(user)) {
			logger.info("Invalid token");
			response.sendRedirect(clientAppUrl + "/invalid-token");
		}else {
			
			logger.info("User: {}, Successful verification", user.getUserDetail().getFirstName());
			user.setConfirmed(true);
			userService.save(user);
			response.sendRedirect(clientAppUrl + "/registration/confirm/" + token);
		}
	}
	
	@PostMapping("/login")
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
	
	@PostMapping("/password/reset/notification")
	public ResponseEntity passwordReset(@Valid @RequestBody PasswordResetNotificationRequest passwordResetNotifRequest) {
		userService.passwordReset(passwordResetNotifRequest);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/password/reset")
	public ResponseEntity passwordReset(@Validated(HospitalValidationSequence.class) @RequestBody PasswordResetRequest passwordResetRequest) {
		boolean isValidResetToken = userService.isValidResetPassToken(passwordResetRequest.getResetToken());
		
		if(isValidResetToken) {
			logger.info("valid reset password token");
			User user = userService.findByResetPassToken(passwordResetRequest.getResetToken()).get();
			user.setDateTimePasswordReset(null);
			user.setResetPassToken(null);
			user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
			userService.save(user);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/password/update/{resetPassToken}")
	public void updatePassword(@PathVariable String resetPassToken, HttpServletResponse response) throws IOException {
		boolean isValid = userService.isValidResetPassToken(resetPassToken);
		
		if(isValid) {
			response.sendRedirect(clientAppUrl + "/password/update/" + resetPassToken);
		}else {
			response.sendRedirect(clientAppUrl + "/invalid-token");
		}
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
