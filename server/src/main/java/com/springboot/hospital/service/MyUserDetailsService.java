package com.springboot.hospital.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.hospital.controller.UserController;
import com.springboot.hospital.dao.UserRepository;
import com.springboot.hospital.entity.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(UserController.class); 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DoctorCodeService doctorCodeService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		logger.info("User '{}' logging in", userName);
		
		Optional<User> user = userRepository.findByEmail(userName);
		
		//Custom message for exceptions is handled in src/main/resources
		User storedUser = user.orElseThrow(() -> new UsernameNotFoundException("Invalid Username and Password"));
		
		if(!isUserValid(storedUser)) {
			throw new UsernameNotFoundException("Invalid Username and Password");
		}
		
		
		return new org.springframework.security.core.userdetails.User(storedUser.getEmail(), storedUser.getPassword(), 
				storedUser.isConfirmed(), !storedUser.isDeleted(), true, true, getAuthorities("USER"));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
	
	private boolean isUserValid(User user) {
		
		if(!User.Type.ALL.contains(user.getUserType())) {
			return false;
		}
		
		Integer doctorCode = user.getUserDetail().getDoctorCodeId();
		if( doctorCode != null) {
			return doctorCodeService.hasCode(String.valueOf(doctorCode));
		}
		
		if(user.getUserDetail() == null ) {
			return false;
		}
		
		return true;
	}

}
