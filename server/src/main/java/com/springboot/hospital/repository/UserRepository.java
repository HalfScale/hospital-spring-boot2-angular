package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
	Optional<User> findByRegistrationToken(String token);
	Optional<User> findByResetPassToken(String token);
}
