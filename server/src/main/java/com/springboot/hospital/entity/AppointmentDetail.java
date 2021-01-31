package com.springboot.hospital.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="appointment_detail")
public class AppointmentDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="appointment_id")
	private Appointment appointment;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
			
	@Column(name="address")
	private String address;
	
	@Column(name="gender")
	private Integer gender;
	
	@Column(name="first_time")
	private boolean firstTime;
	
	@Column(name="appointment_start_date")
	private LocalDate appointmentStartDate;
	
	@Column(name="appointment_start_time")
	private LocalTime appointmentStartTime;
	
	@Column(name="appointment_end_date")
	private LocalDate appointmentEndDate;
	
	@Column(name="appointment_end_time")
	private LocalTime appointmentEndTime;
	
	@Column(name="mobile_no")
	private long mobileNo;
	
	@Column(name="email")
	private String email;
	
	@Column(name="appointment_reason")
	private String appointmentReason;
	
	@Column(name="cancel_reason")
	private String cancelReason;
	
	@Column(name="created")
	private LocalDateTime created;
	
	@Column(name="modified")
	private LocalDateTime modified;
	
	@Column(name="deleted")
	private boolean deleted;
	
	@Column(name="deleted_date")
	private LocalDateTime deletedDate;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy="appointmentDetail", cascade=CascadeType.ALL)
	private List<AppointmentDetailHistory> appointmentDetailHistories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAppointmentReason() {
		return appointmentReason;
	}

	public void setAppointmentReason(String appointmentReason) {
		this.appointmentReason = appointmentReason;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
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

	public List<AppointmentDetailHistory> getAppointmentDetailHistories() {
		return appointmentDetailHistories;
	}

	public void setAppointmentDetailHistories(List<AppointmentDetailHistory> appointmentDetailHistories) {
		this.appointmentDetailHistories = appointmentDetailHistories;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public LocalDate getAppointmentStartDate() {
		return appointmentStartDate;
	}

	public void setAppointmentStartDate(LocalDate appointmentStartDate) {
		this.appointmentStartDate = appointmentStartDate;
	}

	public LocalTime getAppointmentStartTime() {
		return appointmentStartTime;
	}

	public void setAppointmentStartTime(LocalTime appointmentStartTime) {
		this.appointmentStartTime = appointmentStartTime;
	}

	public LocalDate getAppointmentEndDate() {
		return appointmentEndDate;
	}

	public void setAppointmentEndDate(LocalDate appointmentEndDate) {
		this.appointmentEndDate = appointmentEndDate;
	}

	public LocalTime getAppointmentEndTime() {
		return appointmentEndTime;
	}

	public void setAppointmentEndTime(LocalTime appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}
}
