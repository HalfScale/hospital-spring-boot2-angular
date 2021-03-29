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
	
	public String uploadToLocalFileSystem(MultipartFile file) {
		
		String hashedFile = null;
		
		/* we will extract the file name (with extension) from the given file to store it in our local machine for now
        and later in virtual machine when we'll deploy the project
         */
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		/* The Path in which we will store our image . we could change it later
        based on the OS of the virtual machine in which we will deploy the project.
        In my case i'm using windows 10 .
         */
		Path storageDirectory = Paths.get(storageDirectoryPath);
		
		if(!Files.exists(storageDirectory)) {
			try {
				Files.createDirectories(storageDirectory);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			hashedFile = this.hashFile(fileName, file);
			Path destination = Paths.get(storageDirectory.toString() + "\\" + hashedFile);
			Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hashedFile;
	}
	
	public byte[] getImageWithMediaType(String imageName) throws IOException{
		Path destination = Paths.get(storageDirectoryPath + "\\" + imageName);
		
		return IOUtils.toByteArray(destination.toUri());
	}
	
	private String hashFile(String fileName, MultipartFile file) throws NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		Long fileSize = file.getSize();
		
		String modifiedFileName = sb.append("halfscale@gmail.com")
//			.append(file.getOriginalFilename())
//			.append(fileExtension)
//			.append(fileSize)
//			.append(LocalDateTime.now().toString())
			.toString();
		
		return DigestUtils.md5DigestAsHex(modifiedFileName.getBytes());
	}
}
