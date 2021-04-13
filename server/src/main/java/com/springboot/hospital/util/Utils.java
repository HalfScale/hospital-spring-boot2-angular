package com.springboot.hospital.util;

import java.util.HashMap;
import java.util.Map;

import com.springboot.hospital.model.Response;
import com.springboot.hospital.model.UserDetail;

/**
 * 
 * @author MacMuffin
 *
 *	Just a simple helper class
 */
public class Utils {

	public static <T> Response generateResponse(int status, String message, T input) {
		Map<Object, Object> data = new HashMap<>();
		data.put("result", input);
		
		return new Response(status, message, data);
	}
	
	public static String createFullName(UserDetail userDetail) {
		return userDetail.getFirstName() + " " + userDetail.getLastName();
	}
}
