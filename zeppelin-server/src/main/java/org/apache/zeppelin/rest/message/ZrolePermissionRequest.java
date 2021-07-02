package org.apache.zeppelin.rest.message;

import com.google.gson.Gson;

public class ZrolePermissionRequest {
	
	private String roleId;
	private String roleName;
	private String permissionId;
	private String permissionName;
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
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	

	private static final Gson gson = new Gson();

	public String toJson() {
	    return gson.toJson(this);
	}
	
	public static final ZrolePermissionRequest fromJson(String json) {
		 return gson.fromJson(json, ZrolePermissionRequest.class);
	}

}
