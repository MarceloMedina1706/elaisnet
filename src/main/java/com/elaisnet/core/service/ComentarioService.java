package com.elaisnet.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elaisnet.core.model.Comentario;
import com.elaisnet.core.repository.ComentarioRepository;

@Service
public class ComentarioService {

	public long saveComentario(Comentario comentarioPost) {
		
		comentarioRepository.save(comentarioPost);
		
		Comentario c = comentarioRepository.findByIdPostOrderByFechaComentarioDesc(comentarioPost.getIdPost()).get(0);
		
		return c.getIdComentario();
	}
	
	public Comentario getComentarioById(long idComentario) {
		// TODO Auto-generated method stub
		return comentarioRepository.findByIdComentario(idComentario);
	}
	
	@Autowired
    private ComentarioRepository comentarioRepository;

	
}
