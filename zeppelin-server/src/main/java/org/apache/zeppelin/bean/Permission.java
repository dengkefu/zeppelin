package org.apache.zeppelin.bean;

public class Permission {
	
	private String userName;
	private String roleName;
	private String permissionMethod;
	private String permission_uri;
	private String description;
	private String permission_name;
	
	public String getPermission_name() {
		return permission_name;
	}
	
	public String toString() {
		return "["+userName+","+roleName+","+permissionMethod+","+permission_uri+"]";
	}
	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPermissionMethod() {
		return permissionMethod;
	}
	public void setPermissionMethod(String permissionMethod) {
		this.permissionMethod = permissionMethod;
	}
	public String getPermission_uri() {
		return permission_uri;
	}
	public void setPermission_uri(String permission_uri) {
		this.permission_uri = permission_uri;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	

}
