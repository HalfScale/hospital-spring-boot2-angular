package com.springboot.hospital.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.springboot.hospital.validator.SecondLevel;

public class LoginRequest {

	@NotBlank(message="Email is Required")
	@Email(message="Invalid email format", groups = SecondLevel.class)
	private String email;
	
	@NotBlank(message="Password is required")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
