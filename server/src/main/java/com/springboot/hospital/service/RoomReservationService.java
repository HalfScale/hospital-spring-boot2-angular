package com.springboot.hospital.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.hospital.mapper.RoomReservationMapper;
import com.springboot.hospital.model.RoomReservation;
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.dto.AvailableTimeResponseDTO;
import com.springboot.hospital.model.dto.RoomReservationDTO;
import com.springboot.hospital.repository.RoomReservationRepository;
import com.springboot.hospital.util.Utils;

@Service
public class RoomReservationService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RoomReservationService.class);
	
	@Autowired
	private RoomReservationRepository roomReservationRepository;
	
	@Autowired
	private RoomReservationMapper roomReservationMapper;
	
	@Autowired
	private UserService userService;

	public void addRoomReservation(RoomReservationDTO roomReservationDto) {
		
		User currentUser = userService.getCurrentUser().getUser();
		
		RoomReservation roomReservation = roomReservationMapper.map(roomReservationDto);
		roomReservation.setRoomCode(roomReservation.getHospitalRoom().getRoomCode());
		roomReservation.setCreatedBy(currentUser.getId());
		roomReservation.setUpdatedBy(currentUser.getId());
		roomReservation.setCreated(LocalDateTime.now());
		roomReservation.setModified(LocalDateTime.now());
		roomReservation.setReservedByUserId(currentUser.getId());
		
		roomReservationRepository.save(roomReservation);
	}
	
	public LocalDate getAvailableReservationDate() {
		
		RoomReservation roomReservation = roomReservationRepository.findTopByOrderByIdDesc().orElse(null);
		
		if(!Objects.isNull(roomReservation)) {
			
			return roomReservation.getReservedEndDate();
		}
		
		return LocalDate.now();
	}
	
	public List<String> getReservedTime(LocalDate date) {
		
		return roomReservationRepository.findAllByReservedDate(date).stream()
				.map(reservation -> String.format("%s-%s", reservation.getReservedTime(), reservation.getReservedEndTime()))
				.collect(Collectors.toList());
	}
	
	
}
