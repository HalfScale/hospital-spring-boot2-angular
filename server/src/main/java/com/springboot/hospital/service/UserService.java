package com.springboot.hospital.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;

import com.springboot.hospital.entity.User;


public interface UserService {

	void save(User user);
	User update(User user) throws Exception;
	List<User> findAll();
	Optional<User> findById(int id);
	String generateToken();
	User registerNewUserAccount (User user, HttpEntity entity);
	User getVerificationToken(String token);
	
}
