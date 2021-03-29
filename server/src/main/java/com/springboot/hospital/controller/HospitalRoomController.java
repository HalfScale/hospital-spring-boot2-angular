package com.springboot.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.model.HospitalRoom;
import com.springboot.hospital.model.Response;
import com.springboot.hospital.model.dto.HospitalRoomDTO;
import com.springboot.hospital.service.HospitalRoomService;
import com.springboot.hospital.util.Utils;

@RestController
@RequestMapping("/api/rooms")
public class HospitalRoomController {
	
	@Autowired
	private HospitalRoomService hospitalRoomService;
	
	@GetMapping
	public Response getAllRooms() {
		List<HospitalRoom> hospitalRooms = hospitalRoomService.findAll();
		return Utils.<List<HospitalRoom>>generateResponse(0, "Query successful!", hospitalRooms);
	}
	
	@GetMapping("/{roomId}")
	public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
		return ResponseEntity.ok(hospitalRoomService.findById(roomId));
	}
	
	@GetMapping("/pageable")
	public Page<HospitalRoom> loadHospitalRoomPage(Pageable pageable) {
		return hospitalRoomService.findAll(pageable);
	}
	
	@PostMapping
	public ResponseEntity<?> addHospitalRoom(@RequestPart("hospitalRoomDto") String hopsitalRoomDto, 
			@RequestPart("file") MultipartFile file) {
		
		hospitalRoomService.addHospitalRoom(hopsitalRoomDto, file);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{roomId}")
	public ResponseEntity<?> updateRoom(@RequestBody HospitalRoomDTO hopsitalRoomDto, @PathVariable Long roomId) {
		hospitalRoomService.updateHospitalRoom(roomId, hopsitalRoomDto);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
		hospitalRoomService.deleteHospitalRoom(roomId);
		return ResponseEntity.ok().build(); 
	}
	
}
