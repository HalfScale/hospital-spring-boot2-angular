package com.springboot.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.springboot.hospital.entity.EmailConfiguration;

@Service
public class EmailService {

	@Autowired
	private EmailConfiguration emailConfiguration;
	
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(emailConfiguration.getHost());
		mailSenderImpl.setPort(emailConfiguration.getPort());
		mailSenderImpl.setUsername(emailConfiguration.getUsername());
		mailSenderImpl.setPassword(emailConfiguration.getPassword());
		return mailSenderImpl;
	}
	
	public void sendSimpleMessage(String recipient, String sender, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipient);
		message.setSubject(subject);
		message.setText(text);
		message.setFrom(sender);
		getJavaMailSender().send(message);
	}
}
