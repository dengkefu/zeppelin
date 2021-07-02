package org.apache.zeppelin.bean;

import com.google.gson.Gson;

public class Zuser {
	
	private String id;
	private String name;
	private String password;
	private String password_salt;
	private boolean locked;
	private String orgId;
	private String orgName;
	
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword_salt() {
		return password_salt;
	}
	public void setPassword_salt(String password_salt) {
		this.password_salt = password_salt;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	

	private static final Gson gson = new Gson();

	public String toJson() {
	    return gson.toJson(this);
	}
	
	public static final Zuser fromJson(String json) {
		 return gson.fromJson(json,Zuser.class);
	}
}
