package com.springboot.hospital.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RoomReservationDTO {

	private Long hospitalRoomId;
	private boolean hasAssociatedAppointment;
	private LocalDate reservedDate;
	private LocalTime reservedTime;
	private LocalDate reservedEndDate;
	private LocalTime reservedEndTime;
	
	public Long getHospitalRoomId() {
		return hospitalRoomId;
	}
	
	public void setHospitalRoomId(Long hospitalRoomId) {
		this.hospitalRoomId = hospitalRoomId;
	}

	public LocalDate getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(LocalDate reservedDate) {
		this.reservedDate = reservedDate;
	}

	public LocalTime getReservedTime() {
		return reservedTime;
	}

	public void setReservedTime(LocalTime reservedTime) {
		this.reservedTime = reservedTime;
	}

	public LocalDate getReservedEndDate() {
		return reservedEndDate;
	}

	public void setReservedEndDate(LocalDate reservedEndDate) {
		this.reservedEndDate = reservedEndDate;
	}

	public LocalTime getReservedEndTime() {
		return reservedEndTime;
	}

	public void setReservedEndTime(LocalTime reservedEndTime) {
		this.reservedEndTime = reservedEndTime;
	}

	public boolean isHasAssociatedAppointment() {
		return hasAssociatedAppointment;
	}

	public void setHasAssociatedAppointment(boolean hasAssociatedAppointment) {
		this.hasAssociatedAppointment = hasAssociatedAppointment;
	}
	
	
}
