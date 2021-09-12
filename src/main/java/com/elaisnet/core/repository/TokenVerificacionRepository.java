package com.elaisnet.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elaisnet.core.model.VerificationToken;

public interface TokenVerificacionRepository extends JpaRepository<VerificationToken, Long>{
	
	public VerificationToken findByToken(String token);
}
