package com.springboot.hospital.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.hospital.controller.UserController;
import com.springboot.hospital.exception.HospitalException;
import com.springboot.hospital.mapper.UserMapper;
import com.springboot.hospital.model.AuthenticationResponse;
import com.springboot.hospital.model.DoctorCode;
import com.springboot.hospital.model.LoginRequest;
import com.springboot.hospital.model.NotificationEmail;
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.model.dto.PasswordResetNotificationRequest;
import com.springboot.hospital.model.dto.ProfileDTO;
import com.springboot.hospital.model.dto.RefreshTokenRequest;
import com.springboot.hospital.model.dto.RegistrationForm;
import com.springboot.hospital.repository.DoctorCodeRepository;
import com.springboot.hospital.repository.UserDetailRepository;
import com.springboot.hospital.repository.UserRepository;
import com.springboot.hospital.security.JwtProvider;
import com.springboot.hospital.util.Constants;
import com.springboot.hospital.util.Utils;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDetailRepository userDetailRepository;
	@Autowired
	private UserMapper userMapper;
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
	@Autowired
	private MailService mailService;
	@Value("${mail.config.url}")
	private String appUrl;

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
		
//		user.setUserDetail(userDetail);
		userDetail.setUser(user);

		userRepository.save(user);
		
		// Send email
		String subject = "Hospital Account Activation";
		String url = appUrl + "/api/auth/registration/confirm/" + token;
		String message = "Please click the link below to activate your account \n" + url;
		mailService.sendMail(new NotificationEmail(subject, form.getEmail(), message));
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
//			authResponse.setName(user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName());
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
	public boolean isValidToken(User user) {
		int result = LocalDateTime.now().compareTo(user.getCreated().plusDays(1));
		return result == -1;
	}

	@Override
	public void passwordReset(PasswordResetNotificationRequest passwordResetNotifRequest) {
		String subject = "Hospital password reset notification";
		String token = generatePasswordResetToken();
		String url = appUrl + "/api/auth/password/update/" + token;
		String message = "Please click the link below to reset your password \n" + url;
		
		mailService.sendMail(new NotificationEmail(subject, passwordResetNotifRequest.getEmail(), message));
		
		// Make sure to intercept first if the email is valid or not.
		Optional<User> storedUser = userRepository.findByEmail(passwordResetNotifRequest.getEmail());
		storedUser.orElseThrow(() -> new HospitalException("Invalid email!"));
		
		User user = storedUser.get();
		user.setResetPassToken(token);
		user.setDateTimePasswordReset(LocalDateTime.now());
		userRepository.save(user);
	}
	
	@Override
	public boolean isValidResetPassToken(String token) {
		Optional<User> storedUser = userRepository.findByResetPassToken(token);
		if(!storedUser.isPresent()) { return false; }
		
		User user = storedUser.get();
		
		if(user.isDeleted()) { return false; }
		
		int tokenStatus = LocalDateTime.now().compareTo(user.getDatetimePasswordReset().plusDays(1));
		if(tokenStatus > 0) { return false; }
		return true;
	}
	
	@Override
	public Optional<User> findByResetPassToken(String token) {
		return userRepository.findByResetPassToken(token);
	}
	
	@Override
	public User update(User user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String generatePasswordResetToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

	@Override
	public UserDetail getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
				getContext().getAuthentication().getPrincipal();
		
		logger.info("Current user [{}]", principal.getUsername());
		return userDetailRepository.findByUserEmail(principal.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	}

	@Override
	public String getCurrentUserFullName() {
		return Utils.createFullName(this.getCurrentUser());
	}

	@Override
	public void updateProfile(ProfileDTO profileDto) {
		
		if(!this.isLoggedIn()) {
			throw new HospitalException("Invalid access. No user is logged in!");
		}
		
		UserDetail userDetail = this.getCurrentUser();
		
		if(userDetail.getUser().getUserType().equals(Constants.PATIENT) &&
				this.isDoctor(profileDto)) {
			
			throw new HospitalException("Invalid data for user type!");
		}
		
		UserDetail updatedUserDetail = userMapper.map(userDetail, profileDto);
		userDetailRepository.save(updatedUserDetail);
		
	}
	
	private boolean isDoctor(ProfileDTO profileDto) {
		return Arrays.asList(
				profileDto.getSpecialization(),
				profileDto.getNoOfYearsExperience().toString(),
				profileDto.getEducation(),
				profileDto.getDescription()
		).stream().anyMatch(param -> !StringUtils.isBlank(param));
	}
}
