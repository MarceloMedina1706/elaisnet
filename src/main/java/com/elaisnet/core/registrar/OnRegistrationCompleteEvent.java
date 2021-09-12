package com.elaisnet.core.registrar;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.elaisnet.core.model.User;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent{
    
	 public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        
    	super(user);
        
        this.user = user;
        
        this.locale = locale;
        
        this.appUrl = appUrl;
    }
    
    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
    
	private String appUrl;
	
    private Locale locale;
    
    private User user;
}
