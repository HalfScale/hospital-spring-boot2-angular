package com.springboot.hospital.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.springboot.hospital.validator.Extended;

public class RegistrationForm {
	
	@NotBlank(message="First Name is Required")
	private String firstName;
	
	@NotBlank(message="Last Name is Required")
	private String lastName;
	
	@NotBlank(message = "Email is Required")
	@Email(message="Invalid email format", groups = Extended.class)
	private String email;
	
	@Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No")
	@Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits", groups = Extended.class)
	private String mobileNo;
	
	private int gender;
	
	@NotBlank(message="Password is Required")
	private String password;
	
	@NotBlank(message="Confirm password is Required")
	private String confirmPassword;
	
	private String hospitalCode;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "RegistrationForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", mobileNo=" + mobileNo + ", gender=" + gender + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", hospitalCode=" + hospitalCode + "]";
	}
}
