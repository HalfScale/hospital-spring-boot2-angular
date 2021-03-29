package com.springboot.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Integer> {

}
