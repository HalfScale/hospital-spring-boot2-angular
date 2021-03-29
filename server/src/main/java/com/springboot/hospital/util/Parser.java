package com.springboot.hospital.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T parse(String json, Class<T> entity) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(json, entity);
	}
}
