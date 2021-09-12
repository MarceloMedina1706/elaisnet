package com.elaisnet.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elaisnet.core.model.PasswordResetToken;



public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	PasswordResetToken findByToken(String token);
	
	
}
