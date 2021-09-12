package com.elaisnet.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;

import com.elaisnet.core.dto.UserDto;
import com.elaisnet.core.error.UserAlreadyExistException;
import com.elaisnet.core.model.PasswordResetToken;
import com.elaisnet.core.model.Role;
import com.elaisnet.core.model.User;
import com.elaisnet.core.model.VerificationToken;
import com.elaisnet.core.repository.PasswordResetTokenRepository;
import com.elaisnet.core.repository.TokenVerificacionRepository;
import com.elaisnet.core.repository.UserRepository;


@Service
public class UserService {
	
	public User registrarCuenta(UserDto userDto) throws UserAlreadyExistException{
		
		if(emailExist(userDto.getEmail())) {
			throw new UserAlreadyExistException("Ya existe una cuenta con este email: " + userDto.getEmail()); 
		}
		
		User user = new User();
		
		user.setName(userDto.getName());
		
		user.setLastName(userDto.getLastName());
		
		user.setEmail(userDto.getEmail());
		
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		user.setBorn(userDto.getBorn());
		
		user.setSex(userDto.getSex());
		
		user.setProfilePicture("img/profile.jpg");
		
		Role role = new Role();
		
		role.setNameRole("ROLE_USER");
		
		List<Role> roles = new ArrayList<Role>();
		
		roles.add(role);
		
		role.setUser(user);
		
		user.setRole(roles);
		
		return userRepository.save(user);
	}
	
	private boolean emailExist(String email) {
		return userRepository.findByEmail(email) != null;
	}
	
	public void createVerificationToken(String token, User user) {
		
        VerificationToken myToken = new VerificationToken(token, user);
        
        tokenRepository.save(myToken);
		
	}
	
	public VerificationToken getVerificationToken(String token) {
		
		return tokenRepository.findByToken(token);
	}
	
	public void guardarUsuarioRegistrado(User user) {
		
		userRepository.save(user);
	}
	
	public void crearVerificationToken(User user, String token) {

		VerificationToken verificationToken = new VerificationToken(token, user);
		
		tokenRepository.save(verificationToken);
		
	}
	
	public VerificationToken generarNuevoVerificationToken(String token) {
		
		VerificationToken vToken = tokenRepository.findByToken(token);
		
        vToken.updateToken(UUID.randomUUID().toString());
        
        vToken = tokenRepository.save(vToken);
        
        return vToken;
        
	}
	
	public User getUser(String token) {
		
		User user = tokenRepository.findByToken(token).getUser();
		
        return user;   		
	}
	
	public User getUserByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}
	
	public void createPasswordResetTokenForUser(User user, String token) {
		
	    PasswordResetToken myToken = new PasswordResetToken(token, user);
	    
	    passwordTokenRepository.save(myToken);
	}
	
	public Optional<User> getUserByPasswordResetToken(String token) {
		
		return Optional.ofNullable(passwordTokenRepository.findByToken(token) .getUser());
	}
	
	public void changeUserPassword(User user, String newPassword) {
		
		user.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.save(user);
	}
	
	public void updateProfilePicture(User user, String imgProfile) {
		
		user.setProfilePicture(imgProfile);
		
		userRepository.save(user);
	}
	
	public User getUserByIdUser(long idUser) {
		return userRepository.findByIdUser(idUser);
	}
	
	public HashSet<User> getSearch(String search){
		
		HashSet<User> name = userRepository.findByNameContaining(search);
		
		HashSet<User> lastName = userRepository.findByLastNameContaining(search);
		
		for(User user : lastName) {
			
			name.add(user);
		}
		
		
		return name;
	}
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenVerificacionRepository tokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

}
