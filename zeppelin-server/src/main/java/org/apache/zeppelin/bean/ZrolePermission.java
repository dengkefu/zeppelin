package org.apache.zeppelin.bean;

import com.google.gson.Gson;

public class ZrolePermission {
	
	private String roleId;
	private String roleName;
	private String permissionId;
	private String permissionName;
	private String permissionUri;
	private String permissionMethod;
	private String permissionDesc;
	private boolean permissionAvailable;
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
	public String getPermissionUri() {
		return permissionUri;
	}
	public void setPermissionUri(String permissionUri) {
		this.permissionUri = permissionUri;
	}
	public String getPermissionMethod() {
		return permissionMethod;
	}
	public void setPermissionMethod(String permissionMethod) {
		this.permissionMethod = permissionMethod;
	}
	public String getPermissionDesc() {
		return permissionDesc;
	}
	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}
	
	
	public boolean isPermissionAvailable() {
		return permissionAvailable;
	}
	public void setPermissionAvailable(boolean permissionAvailable) {
		this.permissionAvailable = permissionAvailable;
	}


	private static final Gson gson = new Gson();

	public String toJson() {
	    return gson.toJson(this);
	}
	
	public static final ZrolePermission fromJson(String json) {
		 return gson.fromJson(json, ZrolePermission.class);
	}

}
