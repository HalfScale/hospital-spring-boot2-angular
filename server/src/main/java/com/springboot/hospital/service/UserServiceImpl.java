package com.springboot.hospital.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.hospital.controller.UserController;
import com.springboot.hospital.dao.DoctorCodeRepository;
import com.springboot.hospital.dao.UserRepository;
import com.springboot.hospital.dto.RefreshTokenRequest;
import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.DoctorCode;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.entity.UserDetail;
import com.springboot.hospital.security.JwtProvider;

@Service
public class UserServiceImpl implements UserService{
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DoctorCodeRepository doctorCodeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Override
	public void save(User user) {
		userRepository.save(user);
	}
	
	@Override
	public String generateToken() {
		return passwordEncoder.encode(UUID.randomUUID().toString()); 
	}

	@Override
	public User registerNewUserAccount(RegistrationForm form) {
		logger.info("Process registration: {}", form.toString());
		
		User user = new User();
		UserDetail userDetail = new UserDetail();
		
		String token = generateToken();
		
		user.setRegistrationToken(token);
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setCreated(LocalDateTime.now());
		user.setModified(LocalDateTime.now());
		user.setUserType(User.Type.PATIENT);
		
		userDetail.setFirstName(form.getFirstName());
		userDetail.setLastName(form.getLastName());
		userDetail.setMobileNo(form.getMobileNo());
		userDetail.setGender(form.getGender());
		userDetail.setCreated(LocalDateTime.now());
		userDetail.setModified(LocalDateTime.now());
		
		Optional<DoctorCode> doctorCode = doctorCodeRepository.findByDoctorCode(form.getHospitalCode());
		
		if (doctorCode.isPresent()) {
			userDetail.setDoctorCodeId(doctorCode.get().getId());
			user.setUserType(User.Type.DOCTOR);
		}
		
		user.setUserDetail(userDetail);
		userDetail.setUser(user);
		
		
		logger.info("Before saving: {}", user.toString());
		userRepository.save(user);
		return user;
	}
	
	
	@Override
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		
		String token = jwtProvider.generateToken(authenticate);
		String refreshToken = refreshTokenService.generateRefreshToken().getToken();
		
		Optional<User> storedUser = userRepository.findByEmail(loginRequest.getEmail());
		Instant expiresAt = Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis());
		
		AuthenticationResponse authResponse = new AuthenticationResponse(token, loginRequest.getEmail(), refreshToken, expiresAt);
		
		if (storedUser.isPresent()) {
			User user = storedUser.get();
			authResponse.setRole(user.getUserType());
			authResponse.setName(user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName());
		}
		
		return authResponse;
	}

	@Override
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());
		
		Instant expiresAt = Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis());
		return new AuthenticationResponse(token, refreshTokenRequest.getEmail(), refreshTokenRequest.getRefreshToken(), expiresAt);
	}

	@Override
	public boolean isEmailAlreadyInUse(String email) {
		Optional<User> queriedUser = userRepository.findByEmail(email);
		
		if (queriedUser.isPresent() && !queriedUser.get().isDeleted()) { 
			return true; 
		}
		
		return false;
	}

	@Override
	public Optional<User> getVerificationToken(String token) {
		return userRepository.findByRegistrationToken(token);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}

	@Override
	public User update(User user) throws Exception{
		Optional<User> queriedUser = userRepository.findById(user.getId());
		
		if (!queriedUser.isPresent()) {
			throw new Exception("User not found during update");
		}
		
		User persistedUser = queriedUser.get();
		UserDetail persistedUserDetail = persistedUser.getUserDetail();
		
		// Update User's UserDetail
		UserDetail userDetail = user.getUserDetail();
		persistedUserDetail.setFirstName(userDetail.getFirstName());
		persistedUserDetail.setLastName(userDetail.getLastName());
		persistedUserDetail.setMobileNo(userDetail.getMobileNo());
		persistedUserDetail.setGender(userDetail.getGender());
		persistedUserDetail.setAddress(userDetail.getAddress());
		persistedUserDetail.setBirthDate(userDetail.getBirthDate());
		persistedUserDetail.setAddress(userDetail.getAddress());
		persistedUserDetail.setModified(LocalDateTime.now());
		
		// Update User
		persistedUser.setEmail(user.getEmail());
		persistedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		persistedUser.setModified(LocalDateTime.now());
		
		userRepository.save(persistedUser);
		
		logger.info("User data: {}", persistedUser);
		return persistedUser;
	}

	@Override
	public boolean isValidToken(User user) {
		int result = LocalDateTime.now().compareTo(user.getCreated().plusDays(1));
		return result == -1;
	}
}
