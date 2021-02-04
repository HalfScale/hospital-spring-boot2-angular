package com.springboot.hospital.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.hospital.annotation.UniqueEmail;
import com.springboot.hospital.service.UserService;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>{
	
	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && !userService.isEmailAlreadyInUse(value);
	}
}
