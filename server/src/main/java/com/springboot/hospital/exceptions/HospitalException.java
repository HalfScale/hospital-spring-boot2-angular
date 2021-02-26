package com.springboot.hospital.exceptions;

public class HospitalException extends RuntimeException{

	public HospitalException(String message, Exception e) {
		super(message, e);
	}
	
	public HospitalException(String message) {
		super(message);
	}
}
