package com.springboot.hospital.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.entity.HospitalRoom;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {

}
