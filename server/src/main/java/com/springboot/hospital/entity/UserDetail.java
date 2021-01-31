package com.springboot.hospital.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.hospital.validator.Extended;

@Entity
@Table(name="user_details")
public class UserDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id", referencedColumnName = "id")
	@JsonBackReference
	private User user;
	
	@NotBlank(message="First Name is Required")
	@Column(name="first_name")
	private String firstName;
	
	@NotBlank(message="Last Name is Required")
	@Column(name="last_name")
	private String lastName;
	
	@Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No")
	@Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits", groups = Extended.class)
	@NotBlank(message="Mobile No. is Required")
	@Column(name="mobile_no")
	private String mobileNo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="birth_date")
	private LocalDate birthDate;
	
	@NotNull
	@Column(name="gender")
	private int gender;
	
	@Column(name="address")
	private String address;
	
	@Column(name="profile_image")
	private String profileImage;
	
	@Column(name="doctor_code_id")
	private Integer doctorCodeId;
	
	@Column(name="doctor_description")
	private String doctorDescription;
	
	@Column(name="no_of_years_experience")
	private Integer noOfYearsExperience;
	
	@Column(name="education")
	private String education;
	
	@Column(name="schedule")
	private String schedule;
	
	@Column(name="expertise")
	private String expertise;
	
	@Column(name = "created")
	private LocalDateTime created;
	
	@Column(name = "modified")
	private LocalDateTime modified;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@Column(name = "deleted_date")
	private LocalDateTime deletedDate;
	
	public UserDetail() {
		
	}

	public UserDetail(int id, User user, String firstName, String lastName, String mobileNo, LocalDate birthDate,
			int gender, String address, String profileImage, Integer doctorCodeId, String doctorDescription,
			Integer noOfYearsExperience, String education, String schedule, String expertise, LocalDateTime created,
			LocalDateTime modified, boolean deleted, LocalDateTime deletedDate) {
		this.id = id;
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNo = mobileNo;
		this.birthDate = birthDate;
		this.gender = gender;
		this.address = address;
		this.profileImage = profileImage;
		this.doctorCodeId = doctorCodeId;
		this.doctorDescription = doctorDescription;
		this.noOfYearsExperience = noOfYearsExperience;
		this.education = education;
		this.schedule = schedule;
		this.expertise = expertise;
		this.created = created;
		this.modified = modified;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Integer getDoctorCodeId() {
		return doctorCodeId;
	}

	public void setDoctorCodeId(Integer doctorCodeId) {
		this.doctorCodeId = doctorCodeId;
	}

	public String getDoctorDescription() {
		return doctorDescription;
	}

	public void setDoctorDescription(String doctorDescription) {
		this.doctorDescription = doctorDescription;
	}

	public Integer getNoOfYearsExperience() {
		return noOfYearsExperience;
	}

	public void setNoOfYearsExperience(Integer noOfYearsExperience) {
		this.noOfYearsExperience = noOfYearsExperience;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
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

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNo=" + mobileNo
				+ ", birthDate=" + birthDate + ", gender=" + gender + ", address=" + address + ", profileImage="
				+ profileImage + ", doctorCodeId=" + doctorCodeId + ", doctorDescription=" + doctorDescription
				+ ", noOfYearsExperience=" + noOfYearsExperience + ", education=" + education + ", schedule=" + schedule
				+ ", expertise=" + expertise + ", created=" + created + ", modified=" + modified + ", deleted="
				+ deleted + ", deletedDate=" + deletedDate + "]";
	}
}
