package com.springboot.hospital.exception;

public class HospitalDuplicateException extends RuntimeException{

	public HospitalDuplicateException(String message, Exception e) {
		super(message, e);
	}
	
	public HospitalDuplicateException(String message) {
		super(message);
	}
}
