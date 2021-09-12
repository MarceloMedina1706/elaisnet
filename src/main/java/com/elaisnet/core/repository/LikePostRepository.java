package com.elaisnet.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elaisnet.core.model.LikePost;
import com.elaisnet.core.model.Post;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long>{
	
	public abstract LikePost findByIdPost(Post post);
}
