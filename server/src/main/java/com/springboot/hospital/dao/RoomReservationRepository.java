package com.springboot.hospital.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.entity.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Integer> {

}
