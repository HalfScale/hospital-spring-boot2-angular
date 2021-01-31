package com.springboot.hospital.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.entity.HospitalRoom;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Integer> {

}
