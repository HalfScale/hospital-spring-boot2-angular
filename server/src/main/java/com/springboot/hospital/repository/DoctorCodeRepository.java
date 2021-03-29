package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.DoctorCode;

public interface DoctorCodeRepository extends JpaRepository<DoctorCode, Integer> {
	Optional<DoctorCode> findByDoctorCode(String code);
}
