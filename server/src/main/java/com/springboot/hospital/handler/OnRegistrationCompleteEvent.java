package com.springboot.hospital.handler;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.springboot.hospital.entity.User;
import com.springboot.hospital.restcontroller.UserRestController;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);

	private static final long serialVersionUID = 1L;
	private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(
      User user, Locale locale, String appUrl) {
        super(user);
        
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        
        logger.info("Inside OnRegistrationCompleteEvent");
    }

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
