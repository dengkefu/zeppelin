package org.apache.zeppelin.rest.message;

import com.google.gson.Gson;
import org.apache.zeppelin.common.JsonSerializable;

import java.util.List;

public class NewZuserInfoRequest implements JsonSerializable{
	
	public NewZuserInfoRequest() {
		
	}
	
	private String id;
	private String userName;
	private String orgId;
	private String password;
	private boolean locked;
	
	private List<String> roles;
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public static final NewZuserInfoRequest fromJson(String json) {
		 return gson.fromJson(json, NewZuserInfoRequest.class);
	}
}
