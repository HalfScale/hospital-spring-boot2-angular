package com.springboot.hospital.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.entity.DoctorCode;

public interface DoctorCodeRepository extends JpaRepository<DoctorCode, Integer> {
	Optional<DoctorCode> findByDoctorCode(String code);
}
