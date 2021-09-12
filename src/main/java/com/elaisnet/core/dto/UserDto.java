package com.elaisnet.core.dto;


import javax.validation.constraints.*;

import com.elaisnet.core.validation.*;

@PasswordMatches
public class UserDto {
	
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

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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
	
	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String name;

	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String lastName;
	
	@ValidEmail
	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String email;
	
	@ValidPassword
	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String password;
	private String matchingPassword;
	
	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String born;
	
	@NotNull(message="nulo")
	@NotEmpty(message="empty")
	private String sex;
}
