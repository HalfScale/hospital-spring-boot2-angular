package com.springboot.hospital.mapper;

import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.model.dto.ProfileDTO;
import com.springboot.hospital.util.Constants;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
	
	@Value("${file.storage.endpoint}")
	private String imageEndpoint;
	
	@Mapping(target = "firstName", source = "profileDto.firstName")
	@Mapping(target = "lastName", source = "profileDto.lastName")
	@Mapping(target = "mobileNo", source = "profileDto.mobileNo")
	@Mapping(target = "address", source = "profileDto.address")
	@Mapping(target = "profileImage", source = "userDetail.profileImage")
	@Mapping(target = "birthDate", source = "profileDto.birthDate")
	@Mapping(target = "gender", source = "profileDto.gender")
	@Mapping(target = "expertise", source = "profileDto.specialization")
	@Mapping(target = "noOfYearsExperience", source = "profileDto.noOfYearsExperience")
	@Mapping(target = "education", source = "profileDto.education")
	public abstract UserDetail map(UserDetail userDetail, ProfileDTO profileDto);
	
	@Mapping(target = "description", source = "doctorDescription")
	@Mapping(target = "specialization", source = "expertise")
	@Mapping(target = "profileImage", expression = "java(buildImageEndpoint(userDetail))")
	public abstract ProfileDTO mapToProfileDto(UserDetail userDetail);
	
	String buildImageEndpoint(UserDetail userDetail) {
		if(Objects.isNull(userDetail) || Objects.isNull(userDetail.getProfileImage())) {
			return null;
		}
		
		return imageEndpoint + "/" + Constants.PROFILE_IDENTIFIER + "/" + userDetail.getProfileImage();
	}
}
