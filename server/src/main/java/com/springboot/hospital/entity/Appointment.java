package com.springboot.hospital.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="appointment")
public class Appointment {

	public static class Status {

		public static final String APPROVED = "approved";
		public static final String PENDING = "pending";
		public static final String CANCELLED_PATIENT = "cancelled-patient";
		public static final String CANCELLED_HOSPITAL = "cancelled-hospital";

		public static final List<String> _ALL = Arrays.asList(
				APPROVED, CANCELLED_PATIENT, PENDING, CANCELLED_HOSPITAL
		);
	}
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="appointment_status")
	private String appointmentStatus;
	
	@Column(name="created")
	private LocalDateTime created;
	
	@Column(name="modified")
	private LocalDateTime modified;
	
	@Column(name="deleted")
	private boolean deleted;
	
	@Column(name="deleted_date")
	private LocalDateTime deletedDate;
	
	@JsonBackReference
	@OneToMany(fetch=FetchType.LAZY, mappedBy="appointment", cascade=CascadeType.ALL)
	private Set<AppointmentHistory> appointmentHistories;
	
	@JsonBackReference
	@OneToOne(mappedBy="appointment", cascade=CascadeType.ALL)
	private AppointmentDetail appointmentDetail;
	
	@JsonBackReference
	@OneToOne(mappedBy="appointment", cascade=CascadeType.ALL)
	private AppointmentDetailHistory appointmentDetailHistories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
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

	public Set<AppointmentHistory> getAppointmentHistories() {
		return appointmentHistories;
	}

	public void setAppointmentHistories(Set<AppointmentHistory> appointmentHistories) {
		this.appointmentHistories = appointmentHistories;
	}

	public AppointmentDetail getAppointmentDetail() {
		return appointmentDetail;
	}

	public void setAppointmentDetail(AppointmentDetail appointmentDetail) {
		this.appointmentDetail = appointmentDetail;
	}

	public AppointmentDetailHistory getAppointmentDetailHistories() {
		return appointmentDetailHistories;
	}

	public void setAppointmentDetailHistories(AppointmentDetailHistory appointmentDetailHistories) {
		this.appointmentDetailHistories = appointmentDetailHistories;
	}
}
