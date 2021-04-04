package com.springboot.hospital.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.repository.UserDetailRepository;
import com.springboot.hospital.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(UserController.class); 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private DoctorCodeService doctorCodeService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		logger.info("User => [{}] logging in", email);
		
		//Custom message for exceptions is handled in src/main/resources
		UserDetail userDetail = userDetailRepository.findByUserEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid Username and Password"));
		User user = userDetail.getUser();
		
		if(!isUserValid(userDetail)) {
			throw new UsernameNotFoundException("Invalid Username and Password");
		}
		
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), 
				user.isConfirmed(), !user.isDeleted(), true, true, getAuthorities("USER"));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}
	
	private boolean isUserValid(UserDetail userDetail) {
		User user = userDetail.getUser();
		
		if(!User.Type.ALL.contains(user.getUserType())) {
			return false;
		}
		
		Integer doctorCode = userDetail.getDoctorCodeId();
		if(!Objects.isNull(userDetail)) {
			return doctorCodeService.hasCode(String.valueOf(doctorCode));
		}
		
		if(userDetail == null ) {
			return false;
		}
		
		return true;
	}

}
