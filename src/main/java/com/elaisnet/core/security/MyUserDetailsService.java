package com.elaisnet.core.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.Role;
import com.elaisnet.core.model.User;
import com.elaisnet.core.repository.UserRepository;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(mail);
		
		if (user == null) {
			
            throw new UsernameNotFoundException("No se encontró ningún usuario con el correo: " + mail);
        }
		
		boolean accountNonExpired = true;
		
        boolean credentialsNonExpired = true;
        
        boolean accountNonLocked = true;
        
        org.springframework.security.core.userdetails.User ud = 
        		new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), accountNonExpired,
                credentialsNonExpired, accountNonLocked, getAuthorities(user.getRole()));
        
        return ud;
	}
	
	private static List<GrantedAuthority> getAuthorities (List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getNameRole()));
            
        }
        return authorities;
    }
	
	@Autowired
	private UserRepository userRepository;
}
