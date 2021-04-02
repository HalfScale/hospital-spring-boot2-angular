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
	
	@Query("SELECT MAX(room.id) from HospitalRoom room")
	Long findLastId();
	
	@Query("SELECT room FROM HospitalRoom room WHERE (room.roomCode LIKE %?1% "
			+ "OR room.roomName LIKE %?2%) AND room.status = ?3 AND room.deleted = 0")
	Page<HospitalRoom> findAllHospitalRoomsByPage(String roomCode,
			String roomName, Integer status, Pageable pageable);
	
	Page<HospitalRoom> findAllByStatusAndDeletedFalse(Integer status, Pageable pageable);
}
