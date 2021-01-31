package com.springboot.hospital.util;

import java.util.HashMap;
import java.util.Map;

import com.springboot.hospital.entity.Response;

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
}
