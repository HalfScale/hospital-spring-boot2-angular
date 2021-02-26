package com.springboot.hospital.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dao.DoctorCodeRepository;
import com.springboot.hospital.entity.DoctorCode;

@Service
public class DoctorCodeServiceImpl implements DoctorCodeService {
	
	@Autowired
	private DoctorCodeRepository doctorCodeRepository;

	@Override
	public boolean hasCode(String code) {
		Optional<DoctorCode> doctorCode = doctorCodeRepository.findByDoctorCode(code);
		
		if (doctorCode.isPresent() && !doctorCode.get().isDeleted()) {
			return true;
		}
		
		return false;
	}

}