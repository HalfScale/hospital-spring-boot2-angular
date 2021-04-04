package com.springboot.hospital.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.model.dto.ProfileDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mapping(target = "firstName", source = "profileDto.firstName")
	@Mapping(target = "lastName", source = "profileDto.lastName")
	@Mapping(target = "mobileNo", source = "profileDto.mobileNo")
	@Mapping(target = "address", source = "profileDto.address")
	@Mapping(target = "birthDate", source = "profileDto.birthDate")
	@Mapping(target = "gender", source = "profileDto.gender")
	@Mapping(target = "expertise", source = "profileDto.specialization")
	@Mapping(target = "noOfYearsExperience", source = "profileDto.noOfYearsExperience")
	@Mapping(target = "education", source = "profileDto.education")
	UserDetail map(UserDetail userDetail, ProfileDTO profileDto);
}
