package com.springboot.hospital.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.hospital.model.dto.TestDto;
import com.springboot.hospital.service.FileService;
import com.springboot.hospital.util.FileStorageUtil;

@RestController
@RequestMapping("/api/img")
public class ImageController {
	
	private Logger log = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private FileService imageService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private FileStorageUtil fileStorageUtil;
	
	@ResponseBody
	@GetMapping(value = "/{identifier}/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	public byte[] getImageWithMediaType(@PathVariable String imageName, @PathVariable String identifier) throws IOException {
		return imageService.getImageWithMediaType(imageName, identifier);
	}
	
}
