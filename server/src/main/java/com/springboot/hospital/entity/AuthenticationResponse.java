package com.springboot.hospital.entity;

import java.time.Instant;

public class AuthenticationResponse {

	private String authToken;
	private String email;
	private String refreshToken;
	private Instant expiresAt;
	
	public AuthenticationResponse(String authToken, String username, String refreshToken, Instant expiresAt) {
		this.authToken = authToken;
		this.email = username;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	
}
