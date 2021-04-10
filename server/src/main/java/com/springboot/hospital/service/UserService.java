package com.springboot.hospital.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.model.AuthenticationResponse;
import com.springboot.hospital.model.LoginRequest;
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.model.dto.PasswordResetNotificationRequest;
import com.springboot.hospital.model.dto.ProfileDTO;
import com.springboot.hospital.model.dto.RefreshTokenRequest;
import com.springboot.hospital.model.dto.RegistrationForm;


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
	UserDetail getCurrentUser();
	String getCurrentUserFullName();
	void updateProfile(String profileDto, MultipartFile file);
	ProfileDTO getUserProfile();
}
