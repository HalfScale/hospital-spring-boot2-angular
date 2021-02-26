package com.springboot.hospital.entity;

public class AuthenticationResponse {

	private String authToken;
	private String email;
	
	
	public AuthenticationResponse(String authToken, String username) {
		this.authToken = authToken;
		this.email = username;
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
}
