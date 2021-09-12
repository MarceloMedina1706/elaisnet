package com.elaisnet.core.repository;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elaisnet.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByEmail(String email);

	public User findByIdUser(long idUser);
	
	public HashSet<User> findByNameContaining(String name);
	
	public HashSet<User> findByLastNameContaining(String lastName);
}
