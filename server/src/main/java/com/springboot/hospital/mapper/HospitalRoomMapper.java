package com.springboot.hospital.mapper;

import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.springboot.hospital.model.HospitalRoom;
import com.springboot.hospital.model.dto.HospitalRoomDTO;
import com.springboot.hospital.util.Constants;
import com.springboot.hospital.util.FileStorageUtil;

@Mapper(componentModel = "spring")
public abstract class HospitalRoomMapper {
	
	@Value("${file.storage.endpoint}")
	private String imageEndpoint;
	
	public abstract HospitalRoom map(HospitalRoomDTO hospitalRoomDto);
	
	public abstract HospitalRoom mapToOld(HospitalRoom hospitalRoom);
	
	@Mapping(target = "roomImage", expression = "java(buildImageEndpoint(hospitalRoom))")
	public abstract HospitalRoomDTO mapToDto(HospitalRoom hospitalRoom);
	
	String buildImageEndpoint(HospitalRoom hospitalRoom) {
		if(Objects.isNull(hospitalRoom) || Objects.isNull(hospitalRoom.getRoomImage())) {
			return null;
		}
		
		return imageEndpoint + "/" + Constants.HOSPITAL_ROOM_IDENTIFIER + "/" + hospitalRoom.getRoomImage();
	}
	
}
