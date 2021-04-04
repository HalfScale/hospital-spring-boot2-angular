package com.springboot.hospital.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.hospital.exception.HospitalException;
import com.springboot.hospital.model.HospitalRoom;
import com.springboot.hospital.model.RoomReservation;
import com.springboot.hospital.model.dto.RoomReservationDTO;
import com.springboot.hospital.repository.HospitalRoomRepository;

@Mapper(componentModel = "spring")
public abstract class RoomReservationMapper {
	
	@Autowired
	private HospitalRoomRepository hospitalRoomRepository;

	@Mapping(target = "hospitalRoom", expression = "java(getHospitalRoom(roomReservationDTO.getHospitalRoomId()))")
	public abstract RoomReservation map (RoomReservationDTO roomReservationDTO);
	
	HospitalRoom getHospitalRoom(Long hospitalRoomId) {
		return hospitalRoomRepository.findById(hospitalRoomId)
				.orElseThrow(() -> new HospitalException("Invalid hospital room id!"));
	}
}
