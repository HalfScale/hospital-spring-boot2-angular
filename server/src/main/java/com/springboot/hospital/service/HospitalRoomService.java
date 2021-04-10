package com.springboot.hospital.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.hospital.exception.HospitalException;
import com.springboot.hospital.mapper.HospitalRoomMapper;
import com.springboot.hospital.model.HospitalRoom;
import com.springboot.hospital.model.UserDetail;
import com.springboot.hospital.model.dto.HospitalRoomDTO;
import com.springboot.hospital.repository.HospitalRoomRepository;
import com.springboot.hospital.util.Constants;
import com.springboot.hospital.util.FileStorageUtil;
import com.springboot.hospital.util.Parser;

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
	
	public Page<HospitalRoom> findAllByPage(String roomCode, String roomName, 
			Integer status, Pageable pageable) {
		
		roomCode = StringUtils.hasText(roomCode) ? roomCode : null;
		roomName = StringUtils.hasText(roomName) ? roomName: null;
		status = Objects.isNull(status) ? 0 : status;
		
		if(Objects.isNull(roomCode) && Objects.isNull(roomName)) {
			return hospitalRoomRepository.findAllByStatusAndDeletedFalse(status, pageable);
		}
		
		return hospitalRoomRepository.findAllHospitalRoomsByPage(roomCode, roomName, status, pageable);
	}
	
	public void addHospitalRoom(String hospitalRoomDto, MultipartFile uploadedFile) {
		
		if(!userService.isLoggedIn()) {
			throw new HospitalException("User not logged in!");
		}
		
		UserDetail currentUser = userService.getCurrentUser();
		
		try {
			
			HospitalRoom hospitalRoom = hospitalRoomMapper
					.map(Parser.parse(hospitalRoomDto, HospitalRoomDTO.class));
			hospitalRoom.setCreatedBy(currentUser.getUser().getId());
			hospitalRoom.setUpdatedBy(currentUser.getUser().getId());
			hospitalRoom.setCreated(LocalDateTime.now());
			hospitalRoom.setModified(LocalDateTime.now());
			
			//if table is empty
			Long HospitalRoomId = hospitalRoomRepository.save(hospitalRoom).getId();
			
			if(!uploadedFile.isEmpty()) {
				String hashedFile = fileService.id(HospitalRoomId)
						.identifier(fileStorageUtil.getPath(Constants.HOSPITAL_ROOM_IDENTIFIER))
						.file(uploadedFile)
						.upload();
				
				hospitalRoom.setRoomImage(hashedFile);
				hospitalRoomRepository.save(hospitalRoom);
			}
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void updateHospitalRoom(Long id, String hospitalRoomDto, 
			MultipartFile uploadedFile) {
		
		HospitalRoom oldHospitalRoom = hospitalRoomRepository.findByIdAndActive(id)
			.orElseThrow(() -> new HospitalException("Hopsital Room not found!"));
		
		try {
			HospitalRoomDTO parsedHospitalRoomDto = Parser.parse(hospitalRoomDto, HospitalRoomDTO.class);
			HospitalRoom hospitalRoom = hospitalRoomMapper.mapToOld(oldHospitalRoom);
			
			hospitalRoom.setRoomCode(parsedHospitalRoomDto.getRoomCode());
			hospitalRoom.setRoomName(parsedHospitalRoomDto.getRoomName());
			hospitalRoom.setStatus(parsedHospitalRoomDto.getStatus());
			hospitalRoom.setDescription(parsedHospitalRoomDto.getDescription());
			hospitalRoom.setUpdatedBy(userService.getCurrentUser().getUser().getId());
			hospitalRoom.setModified(LocalDateTime.now());
			
			if(!uploadedFile.isEmpty()) {
				String hashedFile = fileService.id(hospitalRoom.getId())
						.identifier(fileStorageUtil.getPath(Constants.HOSPITAL_ROOM_IDENTIFIER))
						.file(uploadedFile)
						.upload();
				
				hospitalRoom.setRoomImage(hashedFile);
			}
			
			hospitalRoomRepository.save(hospitalRoom);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void deleteHospitalRoom(Long id) {
		HospitalRoom hospitalRoom = hospitalRoomRepository.findByIdAndActive(id)
			.orElseThrow(() -> new HospitalException("Hospital Room not found!"));
		
		hospitalRoom.setDeleted(true);
		hospitalRoom.setDeletedDate(LocalDateTime.now());
		hospitalRoom.setModified(LocalDateTime.now());
		hospitalRoom.setUpdatedBy(userService.getCurrentUser().getUser().getId());
		hospitalRoomRepository.save(hospitalRoom);
	}
}
