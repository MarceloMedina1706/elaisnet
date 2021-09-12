package com.elaisnet.core.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elaisnet.core.model.Post;
import com.elaisnet.core.model.User;

public interface PostRepository extends JpaRepository<Post, Long>{
	
	public abstract List<Post> findByIdUserOrderByFechaDesc(User user);
	
	public abstract Post findByIdPost(long IdPost);
	
	@Query(value="SELECT * FROM post WHERE idUser = :myId OR idUser IN :ids ORDER BY fecha DESC", nativeQuery=true)
	public abstract List<Post> findPostFollow(@Param("myId") long myId, @Param("ids") List<Long> ids);
}
