package com.springboot.hospital.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.springboot.hospital.entity.User;
import com.springboot.hospital.restcontroller.UserRestController;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		logger.info("Triggering RegistrationCompleteEvent");
		this.confirmRegistration(event);

	}
	
	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		
		String recipeintAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registration/confirm/" + user.getRegistrationToken();
		
		// Send message to the recipient
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipeintAddress);
		email.setSubject(subject);
		
		String message = "Pleace click the link below to confirm your email address. \n" + "http://localhost:8090" + confirmationUrl;
		
		logger.info("Message: {} sent to: {}", message, user.getEmail());
		
		email.setText(message);
		mailSender.send(email);
	}

}
