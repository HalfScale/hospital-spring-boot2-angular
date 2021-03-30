package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.HospitalRoom;

public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {
	
	@Query("SELECT room FROM HospitalRoom room "
			+ "WHERE room.id = ?1 AND room.deleted = 0")
	Optional<HospitalRoom> findByIdAndActive(Long id);
	
	@Query("SELECT MIN(room.id) from HospitalRoom room")
	Long findLastId();
}
