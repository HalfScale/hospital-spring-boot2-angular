package com.springboot.hospital.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dao.UserRepository;
import com.springboot.hospital.entity.MyUserDetails;
import com.springboot.hospital.entity.User;
import com.springboot.hospital.restcontroller.UserRestController;

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
		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
		
		logger.info("User '{}' successfully logged in.", user.get().getEmail());
		
		
		return user.map(MyUserDetails::new).get();
	}

}
