package com.elaisnet.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Post {
	
	public Post() {}
	
	public long getIdPost() {
		return idPost;
	}

	public void setIdPost(long idPost) {
		this.idPost = idPost;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public User getIdUser() {
		return idUser;
	}

	public void setIdUser(User idUser) {
		this.idUser = idUser;
	}
	
	public List<LikePost> getLikesPost() {
		return likesPost;
	}

	public void setLikesPost(List<LikePost> likesPost) {
		this.likesPost = likesPost;
	}
	
	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPost;
	
	private String contenido;
	
	private String foto;
	
	private Date fecha;
	
	
	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="idUser")
	private User idUser;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idPost",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<LikePost> likesPost;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idPost",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Comentario> comentarios;
}
