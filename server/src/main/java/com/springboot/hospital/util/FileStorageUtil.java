package com.springboot.hospital.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.springboot.hospital.exception.HospitalException;

@Component
public class FileStorageUtil {
	
	@Value("${hospital.room}")
	private String hospitalRoomIdentifier;
	
	public String getPath(String identifier) {
		
		if(identifier.equalsIgnoreCase("hospital-room")) {
			return hospitalRoomIdentifier;
		}
		
		throw new HospitalException("Invalid identifier!");
	}
}
