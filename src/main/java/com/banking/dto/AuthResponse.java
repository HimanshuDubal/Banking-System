package com.banking.dto;

import com.banking.model.Role;

public class AuthResponse {

	private String token;

	private Role role;
	
	private long userId;
	
	public String getToken() {
		return token;
	}
	

	public void setToken(String token) {
		this.token = token;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

}
