package com.springboot.hospital.model.dto;

import java.util.List;

public class AvailableTimeResponseDTO {

	List<String> startTimeCollection;
	
	List<String> endTimeCollection;

	public List<String> getStartTimeCollection() {
		return startTimeCollection;
	}

	public void setStartTimeCollection(List<String> startTimeCollection) {
		this.startTimeCollection = startTimeCollection;
	}

	public List<String> getEndTimeCollection() {
		return endTimeCollection;
	}

	public void setEndTimeCollection(List<String> endTimeCollection) {
		this.endTimeCollection = endTimeCollection;
	}
	
	
}
