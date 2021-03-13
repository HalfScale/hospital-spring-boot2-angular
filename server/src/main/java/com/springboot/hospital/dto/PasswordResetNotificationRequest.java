package com.springboot.hospital.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class PasswordResetNotificationRequest {

	@NotBlank(message = "Email is Required")
	@Email(message = "Email is required")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
