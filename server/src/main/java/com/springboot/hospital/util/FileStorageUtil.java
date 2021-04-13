package com.springboot.hospital.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.springboot.hospital.exception.HospitalException;

@Component
public class FileStorageUtil {
	
	@Value("${hospital.room}")
	private String hospitalRoomIdentifier;
	
	@Value("${profile}")
	private String profileIdentifier;
	
	public String getPath(String identifier) {
		
		if(identifier.equalsIgnoreCase(Constants.HOSPITAL_ROOM_IDENTIFIER)) {
			return hospitalRoomIdentifier;
		}else if (identifier.equalsIgnoreCase(Constants.PROFILE_IDENTIFIER)) {
			return profileIdentifier;
		}
		
		throw new HospitalException("Invalid identifier!");
	}
}
