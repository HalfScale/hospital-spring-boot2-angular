package com.springboot.hospital.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.hospital.model.RoomReservation;
import com.springboot.hospital.repository.RoomReservationRepository;

@ExtendWith(MockitoExtension.class)
class RoomReservationServiceTest {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RoomReservationServiceTest.class);

	@Mock
	private RoomReservationRepository roomReservationRepository;
	
	@InjectMocks
	private RoomReservationService roomReservationService = new RoomReservationService();
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void testGetTime() throws JsonProcessingException {
//		
//		RoomReservation roomReservation = new RoomReservation();
//		roomReservation.setReservedTime(LocalTime.of(10, 0));
//		roomReservation.setReservedEndTime(LocalTime.of(11, 0));
//		
//		RoomReservation roomReservation2 = new RoomReservation();
//		roomReservation2.setReservedTime(LocalTime.of(1, 0));
//		roomReservation2.setReservedEndTime(LocalTime.of(3, 0));
//		
//		List<RoomReservation> reservations = Arrays.asList(roomReservation,
//				roomReservation2);
//		
//		when(roomReservationRepository.findAllByReservedDate(LocalDate.now())).thenReturn(reservations);
//		
//		
//		LOGGER.info("Result => [{}]", objectMapper.writeValueAsString(roomReservationService.getReservedTime()));
//		assertNotNull(roomReservationService.getReservedTime());
	}
}
