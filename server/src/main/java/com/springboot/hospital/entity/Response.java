package com.springboot.hospital.entity;

import java.util.Map;

/**
 * @author MacMuffin
 *
 */
public class Response {

	private int status;
	private String description;
	private Map data;
	
	public Response() {
		
	}
	
	public Response(int status, String description, Map data) {
		this.status = status;
		this.description = description;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Map getData() {
		return data;
	}
	public void setData(Map data) {
		this.data = data;
	}
	
}
