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

import com.springboot.hospital.controller.UserRestController;
import com.springboot.hospital.dao.UserRepository;
import com.springboot.hospital.entity.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class); 
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		logger.info("User '{}' logging in", userName);
		
		Optional<User> user = userRepository.findByEmail(userName);
		
		//Custom message for exceptions is handles in src/main/resources
		User storedUser = user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
		
		
		return new org.springframework.security.core.userdetails.User(storedUser.getEmail(), storedUser.getPassword(), 
				storedUser.isConfirmed(), true, true, true, getAuthorities("USER"));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

}
