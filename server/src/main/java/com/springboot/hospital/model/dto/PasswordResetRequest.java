package com.springboot.hospital.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.springboot.hospital.annotation.FieldsValueMatch;
import com.springboot.hospital.validator.SecondLevel;
import com.springboot.hospital.validator.ThirdLevel;

@FieldsValueMatch(
		message = "Password do not match",
		field = "confirmPassword",
		fieldMatch = "password",
		groups = ThirdLevel.class
	)
public class PasswordResetRequest {
	
	private String resetToken;
	
	@NotBlank(message="Password is Required")
	@Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
	private String password;
	
	@NotBlank(message="Password is Required")
	@Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
	private String confirmPassword;
	
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
