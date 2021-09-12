package com.elaisnet.core.registrar;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.elaisnet.core.model.User;
import com.elaisnet.core.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {

		this.confirmRegistration(event);
		
	}
	
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
    	
        User user = event.getUser();
        
        String token = UUID.randomUUID().toString();
        
        userService.crearVerificationToken(user, token);
        
        String recipientAddress = user.getEmail();
        
        String subject = "ElaisNet - Confirmar registro";
        
        String confirmationUrl 
          = event.getAppUrl() + "/confirmarRegistro?token=" + token;
        
        String message = "Hace click en el enlace para activar tu cuenta";
        
        SimpleMailMessage email = new SimpleMailMessage();
        
        email.setTo(recipientAddress);
        
        email.setSubject(subject);
        
        email.setText(message + "\r\n" + "http://localhost:7171" + confirmationUrl);
        
        mailSender.send(email);
    }
	
	
    @Autowired
    private UserService userService;
 
    @Autowired
    private JavaMailSender mailSender;
}
