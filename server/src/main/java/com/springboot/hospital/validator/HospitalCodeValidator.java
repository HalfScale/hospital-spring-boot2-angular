package com.springboot.hospital.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.hospital.annotation.ValidHospitalCode;
import com.springboot.hospital.service.DoctorCodeService;

public class HospitalCodeValidator implements ConstraintValidator<ValidHospitalCode, String> {
	
	@Autowired
	private DoctorCodeService doctorCodeService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value.isEmpty() || doctorCodeService.hasCode(value);
	}
}
