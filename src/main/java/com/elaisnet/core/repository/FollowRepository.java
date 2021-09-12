package com.elaisnet.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elaisnet.core.model.Follow;
import com.elaisnet.core.model.User;

public interface FollowRepository extends JpaRepository<Follow, Long>{
	
	public abstract Follow findByIdUserAndIdUserFollower(User idUser, User idUserFollower);
	
	public abstract List<Follow> findByIdUserFollower(User idUserFollower);
	
	public abstract List<Follow> findByIdUser(User idUser);
}
