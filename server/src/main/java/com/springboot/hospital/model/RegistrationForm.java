package com.springboot.hospital.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.springboot.hospital.annotation.FieldsValueMatch;
import com.springboot.hospital.annotation.UniqueEmail;
import com.springboot.hospital.annotation.ValidHospitalCode;
import com.springboot.hospital.validator.Extended;
import com.springboot.hospital.validator.SecondLevel;
import com.springboot.hospital.validator.ThirdLevel;

@FieldsValueMatch(
	message = "Password do not match",
	field = "confirmPassword",
	fieldMatch = "password",
	groups = ThirdLevel.class
)
public class RegistrationForm {
	
	@NotBlank(message="First Name is Required")
	private String firstName;
	
	@NotBlank(message="Last Name is Required")
	private String lastName;
	
	@NotBlank(message = "Email is Required")
	@Email(message="Invalid email format", groups = SecondLevel.class)
	@UniqueEmail(message="Email is already in use",groups = ThirdLevel.class)
	private String email;
	
	@NotBlank(message = "Mobile No. is Required")
	@Pattern(regexp = ".*(^[0-9]+$)", message = "Invalid Mobile No", groups = SecondLevel.class)
	@Size(min = 11, max = 13, message = "Mobile No. should be 11-13 digits", groups = ThirdLevel.class)
	private String mobileNo;
	
	private int gender;
	
	@NotBlank(message="Password is Required")
	@Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
	private String password;
	
	@NotBlank(message="Confirm password is Required")
	@Size(min = 6, max = 15, message = "Password should be 6-15 characters", groups = SecondLevel.class)
	private String confirmPassword;
	
	@ValidHospitalCode
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
