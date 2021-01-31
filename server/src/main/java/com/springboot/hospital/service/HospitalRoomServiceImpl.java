package com.springboot.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dao.HospitalRoomRepository;
import com.springboot.hospital.entity.HospitalRoom;

@Service
public class HospitalRoomServiceImpl implements HospitalRoomService {
	
	@Autowired
	private HospitalRoomRepository hospitalRoomRepository;

	@Override
	public List<HospitalRoom> findAll() {
		return hospitalRoomRepository.findAll();
	}

	@Override
	public HospitalRoom findById(int id) {
		return hospitalRoomRepository.findById(id).get();
	}

	@Override
	public void save(HospitalRoom hospitalRoom) {
		hospitalRoomRepository.save(hospitalRoom);
	}

	@Override
	public void deleteById(int id) {
		hospitalRoomRepository.deleteById(id);
	}

}
