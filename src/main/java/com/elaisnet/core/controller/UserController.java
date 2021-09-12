package com.elaisnet.core.controller;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.elaisnet.core.dto.UserDto;
import com.elaisnet.core.model.User;
import com.elaisnet.core.model.VerificationToken;
import com.elaisnet.core.security.UserSecurityService;
import com.elaisnet.core.service.UserService;

@Controller
public class UserController {
	
	@GetMapping("/registrarte")
	public ModelAndView formularioRegistro() {
		
		ModelAndView mv = new ModelAndView();
		
		UserDto userDto = new UserDto();
		
		mv.setViewName("registrarte");
		
		mv.addObject("userDto", userDto);
		
		return mv;		
	}
	
	@GetMapping("/verificarEmail")
	public ModelAndView verficarEmail(@RequestParam(value="recuperacion", required=false) boolean recuperacion) {
		
		ModelAndView mv = new ModelAndView("/verificacionEmail");
		
		mv.addObject("mensaje", "Se registro correctamente. Le enviaremos un mensaje de confirmacion a su direccion de email.");
		
		if(recuperacion) {
			
			mv.addObject("mensaje", "Le enviaremos un mensaje a su direccion de email para que pueda recuperar su cuenta.");
		}
		
		return mv;
	}
	
	@GetMapping("/confirmarRegistro")
	public ModelAndView confirmRegistration
	  (HttpServletRequest request, @RequestParam("token") String token) {
		
		ModelAndView mv = new ModelAndView();
	    
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    
	    if (verificationToken == null) {
	    	
	        String message = "Codigo incorrecto.";
	        
	        mv.addObject("nulo", true);
	        
	        mv.addObject("message", message);
	        
	        mv.setViewName("/badUser");
	        
	        return mv;
	    }
	    
	    User user = verificationToken.getUser();
	    
	    Calendar cal = Calendar.getInstance();
	    
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

	    	mv.addObject("expired", true);
	    	
	    	mv.addObject("token", token);
	    	
	    	mv.addObject("registro", token);
	    	
	    	mv.setViewName("/badUser");
	    	
	    	return mv;
	        
	    } 
	    
	    user.setEnabled(true); 
	    
	    userService.guardarUsuarioRegistrado(user);
	    
	    String path = "src//main//resources//static//img/";
		
		Path directorioImagenes = Paths.get(path);
		
		String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
		
		Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + user.getEmail());
	    
	    File dir = new File(rutaCompleta.toString());
	    
	    dir.mkdir();
	    
	    mv.setViewName("redirect:/index");
        
        return mv;
	    
	}
	
	@GetMapping("/reenviarToken")
	public ModelAndView reenvioToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
	    
		VerificationToken newToken = userService.generarNuevoVerificationToken(existingToken);
	    
	    User user = userService.getUser(newToken.getToken());
	    
	    String appUrl = 
	      "http://" + request.getServerName() + 
	      ":" + request.getServerPort() + 
	      request.getContextPath();
	    
	    SimpleMailMessage email = 
	    	constructResendVerificationTokenEmail(appUrl, newToken, user);
	    
	    mailSender.send(email);
	    
		ModelAndView mv = new ModelAndView("/verificacionEmail");
		
		mv.addObject("mensaje", "Te enviaremos un mensaje con un nuevo token de registro en su cuenta de correo electronico");
		
		return mv;
		
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail
	  (String contextPath, VerificationToken newToken, User user) {
		
	    String confirmationUrl = 
	      contextPath + "/confirmarRegistro?token=" + newToken.getToken();
	    
	    String message = "Hace click en el enlace para activar tu cuenta";
	    
	    SimpleMailMessage email = new SimpleMailMessage();
	    
	    email.setSubject("ElaisNet - Confirmar registro");
	    
	    email.setText(message + " \r\n" + confirmationUrl);
	    
	    email.setTo(user.getEmail());
	    
	    return email;
	}
	
	@GetMapping("/recuperarCuenta")
	public String forgotPassword() {
		
		return "forgotPassword";
	}
	
	@GetMapping("/changePassword")
	public ModelAndView showChangePasswordPage(ModelMap model, 
	  @RequestParam("token") String token) {
		
	    String result = securityService.validatePasswordResetToken(token);
	    
	    if(result != null) {
	    	
	    	if(result.equals("invalidToken")) {
	    		
	    		String message = "Codigo incorrecto.";
		        
	    		model.addAttribute("nulo", true);
		        
	    		model.addAttribute("message", message);
	    		
	    	}else if(result.equals("expired")){
	    		
	    		model.addAttribute("expired", true);
		    	
	    		model.addAttribute("token", token);
	    	}
	        
	        return new ModelAndView("/badUser", model);
	        
	    } else {
	    	
	    	User user = securityService.getUserByPasswordToken(token);
	    	
	        model.addAttribute("token", token);
	        
	        model.addAttribute("firstName", user.getName());
	        
	        model.addAttribute("lastName", user.getLastName());
	        
	        return new ModelAndView("/resetPassword");
	    }
	}
	
	@GetMapping("/badResetPassword")
	public ModelAndView badResetPassword (@RequestParam("token") String token,
			@RequestParam(value="expired", required=false) boolean expired,
			@RequestParam(value="nulo", required=false) boolean nulo){
		
		ModelAndView mav = new ModelAndView();
		
		if(expired) mav.addObject("expired", true);
		
		if(nulo) {
		
			mav.addObject("nulo", true);
			
			mav.addObject("message", "Codigo incorrecto.");	
		}
		
		mav.addObject("token", token);
		
		mav.setViewName("/badUser");
		
		return mav;
	}
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserSecurityService securityService;
}
