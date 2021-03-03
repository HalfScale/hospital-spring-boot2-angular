package com.springboot.hospital.service;

import java.util.List;
import java.util.Optional;

import com.springboot.hospital.entity.AuthenticationResponse;
import com.springboot.hospital.entity.LoginRequest;
import com.springboot.hospital.entity.RegistrationForm;
import com.springboot.hospital.entity.User;


public interface UserService {

	void save(User user);
	User update(User user) throws Exception;
	List<User> findAll();
	Optional<User> findById(int id);
	String generateToken();
	User registerNewUserAccount (RegistrationForm form);
	Optional<User> getVerificationToken(String token);
	boolean isEmailAlreadyInUse(String email);
	AuthenticationResponse login(LoginRequest loginRequest);
	boolean isValidToken(User user);
}
