package com.elaisnet.core.model;


import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {
	
	public User() {
		this.enabled = false;
	}
	
	public long getIdUser() {
		return idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBorn() {
		return born;
	}

	public void setBorn(String born) {
		this.born = born;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}
	
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
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
	private long idUser;
	
	private String name;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String born;
	
	private String sex;
	
	private String profilePicture;
	
	private boolean enabled;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="user",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Role> role;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idUser",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Post> posts;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idUser",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<LikePost> likesPost;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idUser",
			cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Comentario> comentarios;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="idUser")
	private List<Follow> follows;
}
