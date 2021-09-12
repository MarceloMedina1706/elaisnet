package com.elaisnet.core.security;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.PasswordResetToken;
import com.elaisnet.core.model.User;
import com.elaisnet.core.repository.PasswordResetTokenRepository;

@Service
@Transactional
public class UserSecurityService{

    public String validatePasswordResetToken(String token) {
    	
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        
        return !isTokenFound(passToken) ? "Token inválido"
                : isTokenExpired(passToken) ? "El token expiró"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
    	
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
    	
        Calendar cal = Calendar.getInstance();
        
        return passToken.getExpiryDate().before(cal.getTime());
    }
    
    public User getUserByPasswordToken(String token) {
    	
    	return passwordTokenRepository.findByToken(token).getUser();
    }
    
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

}
