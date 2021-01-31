package com.springboot.hospital.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="room_reservation")
public class RoomReservation {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne(cascade= CascadeType.ALL)
	@JsonBackReference
	@JoinColumn(name="hospital_room_id")
	private HospitalRoom hospitalRoom;
	
	@Column(name="room_code")
	private String roomCode;
	
	@Column(name="reserved_by_user_id")
	private String reservedByUserId;
	
	@Column(name="has_associated_appointment")
	private boolean hasAssociatedAppointment;
	
	@Column(name="associated_appointment_id")
	private Integer associatedAppointmentId;
	
	@Column(name="reserved_date")
	private LocalDate reservedDate;
	
	@Column(name="reserved_time")
	private LocalTime reservedTime;
	
	@Column(name="reserved_end_date")
	private LocalDate reservedEndDate;
	
	@Column(name="reserved_end_time")
	private LocalTime reservedEndTime;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name="reservation_status")
	private int reservationStatus;
	
	@Column(name="created")
	private LocalDateTime created;
	
	@Column(name="modified")
	private LocalDateTime modified;
	
	@Column(name="deleted")
	private boolean deleted;
	
	@Column(name="deleted_date")
	private LocalDateTime deletedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HospitalRoom getHospitalRoom() {
		return hospitalRoom;
	}

	public void setHospitalRoom(HospitalRoom hospitalRoom) {
		this.hospitalRoom = hospitalRoom;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getReservedByUserId() {
		return reservedByUserId;
	}

	public void setReservedByUserId(String reservedByUserId) {
		this.reservedByUserId = reservedByUserId;
	}

	public boolean isHasAssociatedAppointment() {
		return hasAssociatedAppointment;
	}

	public void setHasAssociatedAppointment(boolean hasAssociatedAppointment) {
		this.hasAssociatedAppointment = hasAssociatedAppointment;
	}

	public Integer getAssociatedAppointmentId() {
		return associatedAppointmentId;
	}

	public void setAssociatedAppointmentId(Integer associatedAppointmentId) {
		this.associatedAppointmentId = associatedAppointmentId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(int reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public LocalDateTime getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(LocalDateTime deletedDate) {
		this.deletedDate = deletedDate;
	}
	
	
}
