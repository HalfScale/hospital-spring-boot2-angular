package com.springboot.hospital.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	private final Logger log = LoggerFactory.getLogger(FileService.class);
	
	@Value("${storage.directory.path}")
	private String storageDirectoryPath;
	
	private Long entityId;
	private String currentUser;
	private String identifier = "\\";
	private MultipartFile file;

	public FileService id(Long id) {
		this.setId(id);
		return this;
	}
	
	public FileService user(String currentUser) {
		log.info("user() => [{}]", currentUser);
		this.setCurrentUser(currentUser);
		return this;
	}
	
	public FileService identifier(String identifier) {
		this.setIdentifier(identifier);
		return this;
	}
	
	public FileService file(MultipartFile file) {
		this.setFile(file);
		return this;
	}
	
	public String upload() {
		return this.uploadToLocalFileSystem();
	}
	
	private void setId(Long id) {
		this.entityId = id;
	}


	private void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}


	private  void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	private void setFile(MultipartFile file) {
		this.file = file;
	}

	private String uploadToLocalFileSystem() {
		
		String hashedFile = null;
		
		/* we will extract the file name (with extension) from the given file to store it in our local machine for now
        and later in virtual machine when we'll deploy the project
         */
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		/* The Path in which we will store our image . we could change it later
        based on the OS of the virtual machine in which we will deploy the project.
        In my case i'm using windows 10 .
         */
		log.info("uploadToLocalFileSystem path => [{}]", this.storageDirectoryPath);
		
		Path storageDirectory = Paths.get(this.storageDirectoryPath + this.identifier);
		
		if(!Files.exists(storageDirectory)) {
			try {
				Files.createDirectories(storageDirectory);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		log.info("uploadToLocalFileSystem path => [{}]", storageDirectory.toString());
		
		try {
			hashedFile = this.hashFile();
			Path destination = Paths.get(storageDirectory.toString() + "\\" + hashedFile);
			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hashedFile;
	}
	
	private String hashFile() throws NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		
		String modifiedFileName = sb.append(this.entityId)
			.append(this.currentUser).toString();
		
		return DigestUtils.md5DigestAsHex(modifiedFileName.getBytes());
	}
	
	public byte[] getImageWithMediaType(String imageName) throws IOException{
		Path destination = Paths.get(this.storageDirectoryPath + "\\" + imageName);
		
		return IOUtils.toByteArray(destination.toUri());
	}
	
}
