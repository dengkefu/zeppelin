package org.apache.zeppelin.rest.message;

import com.google.gson.Gson;

public class ZpermissionRequest {
	
	private String permissionId;
	private String permissionName;
	private String permissionUri;
	private String permissionMethod;
	
	private String description;
	
	private boolean available;

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
	
	public static final ZpermissionRequest fromJson(String json) {
		 return gson.fromJson(json, ZpermissionRequest.class);
	}

}
