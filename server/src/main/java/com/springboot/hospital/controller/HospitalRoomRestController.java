package com.springboot.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.entity.HospitalRoom;
import com.springboot.hospital.entity.Response;
import com.springboot.hospital.service.HospitalRoomService;
import com.springboot.hospital.util.Utils;

@RestController
public class HospitalRoomRestController {
	
	@Autowired
	private HospitalRoomService hospitalRoomService;
	
	@GetMapping("/api/rooms")
	public Response getAllRooms() {
		List<HospitalRoom> hospitalRooms = hospitalRoomService.findAll();
		return Utils.<List<HospitalRoom>>generateResponse(0, "Query successful!", hospitalRooms);
	}
	
	@GetMapping("/api/test/rooms")
	public Page<HospitalRoom> loadHospitalRoomPage(Pageable pageable) {
		return hospitalRoomService.findAll(pageable);
	}
}
