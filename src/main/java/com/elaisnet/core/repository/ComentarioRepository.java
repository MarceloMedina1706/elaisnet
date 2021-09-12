package com.elaisnet.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elaisnet.core.model.Comentario;
import com.elaisnet.core.model.Post;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
	
	public abstract List<Comentario> findByIdPostOrderByFechaComentarioDesc(Post post);
	
	public abstract Comentario findByIdComentario(long idComentario);
}
