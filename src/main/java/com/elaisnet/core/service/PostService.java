package com.elaisnet.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.Post;
import com.elaisnet.core.model.User;
import com.elaisnet.core.repository.PostRepository;

@Service
public class PostService {
	
	public long savePost(Post post) {
		postRepository.save(post);
		
		Post p = postRepository.findByIdUserOrderByFechaDesc(post.getIdUser()).get(0);
		
		return p.getIdPost();
	}
	
	public List<Post> getPostsByUser(User user){
		
		return postRepository.findByIdUserOrderByFechaDesc(user);
	}
	
	public Post getPostById(long idPost) {
		
		return postRepository.findByIdPost(idPost);
	}
	
	public void deletePostByIdPost(long idPost) {
		
		postRepository.deleteById(idPost);
	}
	
	public List<Post> getPostFollow(long myId, List<Long> ids){
		return postRepository.findPostFollow(myId, ids);
	}
	
	
	@Autowired
	private PostRepository postRepository;


	



	
}
