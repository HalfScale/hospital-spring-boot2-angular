package com.springboot.hospital.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.hospital.exception.HospitalException;
import com.springboot.hospital.mapper.HospitalRoomMapper;
import com.springboot.hospital.model.HospitalRoom;
import com.springboot.hospital.model.User;
import com.springboot.hospital.model.dto.HospitalRoomDTO;
import com.springboot.hospital.repository.HospitalRoomRepository;
import com.springboot.hospital.util.Constants;
import com.springboot.hospital.util.FileStorageUtil;
import com.springboot.hospital.util.Parser;
import com.springboot.hospital.util.Utils;

@Service
public class HospitalRoomService {
	
	Logger logger = LoggerFactory.getLogger(HospitalRoomService.class);
	
	@Autowired
	private HospitalRoomRepository hospitalRoomRepository;
	
	@Autowired
	private HospitalRoomMapper hospitalRoomMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileStorageUtil fileStorageUtil;
	
	public List<HospitalRoom> findAll() {
		return hospitalRoomRepository.findAll();
	}
	
	public HospitalRoomDTO findById(Long id) {
		return hospitalRoomMapper.mapToDto(hospitalRoomRepository.findByIdAndActive(id)
				.orElseThrow(() -> new HospitalException("Hospital Room not found!")));
	}
	
	public Page<HospitalRoom> findAll(Pageable pageable) {
		return hospitalRoomRepository.findAll(pageable);
	}
	
	public void addHospitalRoom(String hospitalRoomDto, MultipartFile file) {
		
		if(!userService.isLoggedIn()) {
			throw new HospitalException("User not logged in!");
		}
		
		User currentUser = userService.getCurrentUser();
		String fullName = userService.getCurrentUserFullName();
		
		try {
			
			HospitalRoom hospitalRoom = hospitalRoomMapper
					.map(Parser.parse(hospitalRoomDto, HospitalRoomDTO.class));
			hospitalRoom.setCreatedBy(fullName);
			hospitalRoom.setUpdatedBy(fullName);
			hospitalRoom.setCreated(LocalDateTime.now());
			hospitalRoom.setModified(LocalDateTime.now());
			
			Long lastHospitalRoomId = hospitalRoomRepository.findLastId();
			String hashedFile = fileService.id(lastHospitalRoomId + 1)
					.user(currentUser.getEmail())
					.identifier(fileStorageUtil.getPath(Constants.HOSPITAL_ROOM_IDENTIFIER))
					.file(file)
					.upload();
			
			hospitalRoom.setRoomImage(hashedFile);
			hospitalRoomRepository.save(hospitalRoom);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void updateHospitalRoom(Long id, HospitalRoomDTO hospitalRoomDto) {
		HospitalRoom oldHospitalRoom = hospitalRoomRepository.findByIdAndActive(id)
			.orElseThrow(() -> new HospitalException("Hopsital Room not found!"));
		
		HospitalRoom hospitalRoom = hospitalRoomMapper.mapToOld(oldHospitalRoom);
		hospitalRoom.setRoomCode(hospitalRoomDto.getRoomCode());
		hospitalRoom.setRoomName(hospitalRoomDto.getRoomName());
		hospitalRoom.setStatus(hospitalRoomDto.getStatus());
		hospitalRoom.setDescription(hospitalRoomDto.getDescription());
		hospitalRoom.setUpdatedBy("Test User");
		hospitalRoom.setModified(LocalDateTime.now());
		
		hospitalRoomRepository.save(hospitalRoom);
	}
	
	public void deleteHospitalRoom(Long id) {
		HospitalRoom hospitalRoom = hospitalRoomRepository.findByIdAndActive(id)
			.orElseThrow(() -> new HospitalException("Hospital Room not found!"));
		
		hospitalRoom.setDeleted(true);
		hospitalRoom.setDeletedDate(LocalDateTime.now());
	}
}
