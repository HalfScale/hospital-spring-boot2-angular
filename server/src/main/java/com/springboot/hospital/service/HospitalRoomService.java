package com.springboot.hospital.service;

import java.util.List;

import com.springboot.hospital.entity.HospitalRoom;

public interface HospitalRoomService {

	List<HospitalRoom> findAll();
	HospitalRoom findById(int id);
	void save(HospitalRoom hospitalRoom);
	void deleteById(int id);
}
