package com.elaisnet.core.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.elaisnet.core.dto.UserDto;
import com.elaisnet.core.error.UserAlreadyExistException;
import com.elaisnet.core.model.RespuestaGenerica;
import com.elaisnet.core.model.User;
import com.elaisnet.core.registrar.OnRegistrationCompleteEvent;
import com.elaisnet.core.security.UserSecurityService;
import com.elaisnet.core.service.UserService;
import com.elaisnet.core.dto.PasswordDto;

@RestController
public class RestUserController {
	
	
	/*@RequestMapping(value = "/registrarte", method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)*/
	@PostMapping("/registrarte")
	public RespuestaGenerica[] registrarUsuario(@Valid @RequestBody UserDto userDto, 
			Errors errors, HttpServletRequest request) {
		
		if(!errors.hasErrors()) {
		
			try {
				
				User registrado = userService.registrarCuenta(userDto);
		        
		        String appUrl = request.getContextPath();
		        
		        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registrado, 
		          request.getLocale(), appUrl));
				
				RespuestaGenerica respuestaGenerica[] = {new RespuestaGenerica("no-error", "")};
				
				return respuestaGenerica;
				
			}catch(UserAlreadyExistException e) {
				
				e.printStackTrace();
				
				RespuestaGenerica respuestaGenerica[] = {new RespuestaGenerica("email", e.getMessage())};
				
				return respuestaGenerica;
			}
			
			
		
		}else {
			
			int i=0;
			
			RespuestaGenerica respuestaGenerica[] = new RespuestaGenerica[10]; 
			
			for(FieldError fe : errors.getFieldErrors()) {
				
				respuestaGenerica[i] = new RespuestaGenerica(fe.getField(), fe.getDefaultMessage());
				System.out.printf("fiel: %s \t message: %s\n", fe.getField(), fe.getDefaultMessage());
				i++;
			}
			
			List<ObjectError> oe = errors.getAllErrors();
			
			for(ObjectError ooee : oe) {
				
				if(ooee.getCode().equalsIgnoreCase("PasswordMatches")) {
					
					respuestaGenerica[i] = new RespuestaGenerica("password", ooee.getDefaultMessage());
				}
			}
			
			return respuestaGenerica;
			
		}
		
	}
	
	//====================RESETPASSWORD==================================
	
	@PostMapping("/resetPassword")
	public boolean resetPassword(HttpServletRequest request, 
	  @RequestParam("email") String userEmail){
		
	    User user = userService.getUserByEmail(userEmail);
	    
	    if (user == null) {
	    	
	        return false;
	    }
	    
	    String appUrl = 
	  	      "http://" + request.getServerName() + 
	  	      ":" + request.getServerPort() + 
	  	      request.getContextPath();
	    
	    String token = UUID.randomUUID().toString();
	    
	    userService.createPasswordResetTokenForUser(user, token);
	    
	    mailSender.send(constructResetTokenEmail(appUrl, token, user));
	    
	    return true;
	}
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, User user) {
		
		String url = contextPath + "/changePassword?token=" + token;
		
		String message = "Recupera tu cuenta haciendo click en el siguiente enlace:";
		
		String subject = "ElaisNet - Restablecer la contraseña";
		
		return constructEmail(subject, message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		
		SimpleMailMessage email = new SimpleMailMessage();
		
		email.setSubject(subject);
		
		email.setText(body);
		
		email.setTo(user.getEmail());
		
		return email;
	}
	//====================ENDRESETPASSOWORD==============================
	
	@PostMapping("/savePassword")
	public String savePassword(@Valid PasswordDto passwordDto, Errors errors) {
		
		if(errors.hasErrors()) {
			
			String mensaje = "";
			
			String msg = errors.getFieldError().getDefaultMessage();
			
			String msgs[] = msg.split("\n");
			
			for(int i=0; i<msgs.length; i++) {
				
				if(msgs[i].equals("Password must be at least 6 characters in length.")) {
					
					mensaje += "La contraseña debe contener al menos 6 caracteres\n";
					
				}else if(msgs[i].equals("Password must contain at least 1 uppercase characters.")) {
					
					mensaje += "La contraseña debe contener al menos 1 mayúscula\n";
					
				}else if(msgs[i].equals("Password must contain at least 1 digit characters.")) {
					
					mensaje += "La contraseña debe contener al menos 1 dígito.\n";
					
				}if(msgs[i].equals("Password must contain at least 1 special characters.")) {
					
					mensaje += "La contraseña debe contener al menos 1 carácter especial.\n";
					
				}
				
			}
			
			return mensaje;
		}

	    String result = securityService.validatePasswordResetToken(passwordDto.getToken());
	    
	    if(result != null) {
	    	
	        return result;
	    }

	    Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
	    
	    if(user.isPresent()) {
	    	
	        userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
	        
	        return "saved";
	        
	    }
		return null;
	}
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private UserSecurityService securityService;

}
