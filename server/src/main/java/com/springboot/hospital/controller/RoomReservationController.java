package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.dto.RoomReservationDTO;
import com.springboot.hospital.service.RoomReservationService;

@RestController
@RequestMapping("/api/reservations")
public class RoomReservationController {
	
	@Autowired
	private RoomReservationService roomReservationService;

	@PostMapping
	public ResponseEntity<?> addRoomReservation(@RequestBody RoomReservationDTO roomReservationDto) {
		roomReservationService.addRoomReservation(roomReservationDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
