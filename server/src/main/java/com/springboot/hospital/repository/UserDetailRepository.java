package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.UserDetail;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

	Optional<UserDetail> findByUserEmail(String email);
}
