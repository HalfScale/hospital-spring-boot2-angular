package com.springboot.hospital.service;

import java.util.List;
import java.util.Optional;

import com.springboot.hospital.model.AuthenticationResponse;
import com.springboot.hospital.model.LoginRequest;
import com.springboot.hospital.model.RegistrationForm;
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.dto.PasswordResetNotificationRequest;
import com.springboot.hospital.model.dto.RefreshTokenRequest;


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
	AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
	void passwordReset(PasswordResetNotificationRequest passwordResetNotifRequest);
	Optional<User> findByResetPassToken(String token);
	boolean isValidResetPassToken(String token);
	boolean isLoggedIn();
	User getCurrentUser();
}
