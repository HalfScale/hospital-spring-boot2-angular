package com.springboot.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.UserDetail;

public interface UserDetailRepository {

<<<<<<< Updated upstream
=======
	Optional<UserDetail> findByUserEmail(String email);
	
	Optional<UserDetail> findByUserId(Long id);
>>>>>>> Stashed changes
}
