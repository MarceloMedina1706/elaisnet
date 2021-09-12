package com.elaisnet.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.LikePost;
import com.elaisnet.core.model.Post;
import com.elaisnet.core.repository.LikePostRepository;

@Service
public class LikePostService{
	
	public void saveLikePost(LikePost likePost) {
		likePostRepository.save(likePost);
	}
	
	public void deleteLikePost(Post post) {
		
		LikePost likePost = likePostRepository.findByIdPost(post);
		
		likePostRepository.delete(likePost);
	}
	
	@Autowired
	private LikePostRepository likePostRepository;
}
