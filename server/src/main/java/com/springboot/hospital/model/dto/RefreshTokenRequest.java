package com.springboot.hospital.model.dto;

import javax.validation.constraints.NotBlank;

public class RefreshTokenRequest {

	@NotBlank
	private String refreshToken;
	private String email;
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
