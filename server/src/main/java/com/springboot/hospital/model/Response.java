package com.springboot.hospital.model;

import java.util.Map;

/**
 * @author MacMuffin
 *
 */
public class Response {

	private int status;
	private String message;
	private Map data;
	
	public Response() {
		
	}
	
	public Response(int status, String message, Map data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setDescription(String description) {
		this.message = description;
	}
	public Map getData() {
		return data;
	}
	public void setData(Map data) {
		this.data = data;
	}
	
}
