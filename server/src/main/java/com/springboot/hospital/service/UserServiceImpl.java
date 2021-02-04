package com.springboot.hospital.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dao.DoctorCodeRepository;
import com.springboot.hospital.dao.UserRepository;
import com.springboot.hospital.entity.DoctorCode;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.entity.UserDetail;
import com.springboot.hospital.restcontroller.UserRestController;

@Service
public class UserServiceImpl implements UserService{
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DoctorCodeRepository doctorCodeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
	public boolean isEmailAlreadyInUse(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	@Override
	public User getVerificationToken(String token) {
		return userRepository.findByRegistrationToken(token).get();
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
	
}
