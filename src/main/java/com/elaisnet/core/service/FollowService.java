package com.elaisnet.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.Follow;
import com.elaisnet.core.model.User;
import com.elaisnet.core.repository.FollowRepository;

@Service
public class FollowService {
	
	public void saveFollow(Follow follow) {
		followRepository.save(follow);
	}
	
	public void deleteFollow(Follow follow) {
		followRepository.delete(follow);
	}
	
	public Follow findFollow(User idUser, User idUserFollower) {
		
		return followRepository.findByIdUserAndIdUserFollower(idUser, idUserFollower);
		
	}
	
	public List<Follow> getFollowByIdUserFollower(User userFollower){
		
		return followRepository.findByIdUserFollower(userFollower);
	}
	
	public List<Follow> getFollowByIdUser(User idUser){
		
		return followRepository.findByIdUser(idUser);
	}
	
	@Autowired
	private FollowRepository followRepository;
}
