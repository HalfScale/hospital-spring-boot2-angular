package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.HospitalRoom;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {
	
	@Query("SELECT room FROM HospitalRoom room "
			+ "WHERE room.id = ?1 AND room.deleted = 0")
	Optional<HospitalRoom> findByIdAndActive(Long id);
	
	@Query("SELECT room FROM HospitalRoom room WHERE (room.roomCode LIKE %?1% "
			+ "OR room.roomName LIKE %?2%) AND room.deleted = 0")
	Page<HospitalRoom> findAllHospitalRoomsByPage(String roomCode,
			String roomName, Pageable pageable);
	
	Page<HospitalRoom> findAllByDeletedFalse(Pageable pageable);
	
	Optional<HospitalRoom> findByRoomCodeAndRoomName(String roomCode, String roomName);
	
	Optional<HospitalRoom> findByRoomCode(String roomCode);
	
	Optional<HospitalRoom> findByRoomName(String roomName);
}
