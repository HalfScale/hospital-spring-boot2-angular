package com.springboot.hospital.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Integer> {
	
	Optional<RoomReservation> findTopByOrderByIdDesc();
	
	List<RoomReservation> findAllByReservedDate(LocalDate reservedDate);
}
