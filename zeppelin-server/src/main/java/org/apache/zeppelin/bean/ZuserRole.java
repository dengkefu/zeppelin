package org.apache.zeppelin.bean;

import com.google.gson.Gson;

public class ZuserRole {
	
	private String userId;
	private String userName;
	private String roleId;
	private String roleName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	

	private static final Gson gson = new Gson();

	public String toJson() {
	    return gson.toJson(this);
	}
	
	public static final ZuserRole fromJson(String json) {
		 return gson.fromJson(json, ZuserRole.class);
	}
	
	

}
