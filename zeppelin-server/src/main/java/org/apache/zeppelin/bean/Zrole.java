package org.apache.zeppelin.bean;

import com.google.gson.Gson;

public class Zrole {
	
	private String id;
	private String role;
	private String description;
	private boolean available;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	private static final Gson gson = new Gson();

	public String toJson() {
	    return gson.toJson(this);
	}
	
	public static final Zrole fromJson(String json) {
		 return gson.fromJson(json,Zrole.class);
	}

}
