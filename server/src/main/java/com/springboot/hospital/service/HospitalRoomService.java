package com.springboot.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dao.HospitalRoomRepository;
import com.springboot.hospital.entity.HospitalRoom;

@Service
public class HospitalRoomService {
	
	@Autowired
	private HospitalRoomRepository hospitalRoomRepository;

	public List<HospitalRoom> findAll() {
		return hospitalRoomRepository.findAll();
	}
	
	public Page<HospitalRoom> findAll(Pageable pageable) {
		return hospitalRoomRepository.findAll(pageable);
	}
}
