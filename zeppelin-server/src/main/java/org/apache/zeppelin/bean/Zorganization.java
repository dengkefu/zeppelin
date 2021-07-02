package org.apache.zeppelin.bean;

import com.google.gson.Gson;

public class Zorganization {
	
	private String orgId;
	private String orgName;
	private String parentId;
	private String orgDesc;
	private boolean available;
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getOrgDesc() {
		return orgDesc;
	}
	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
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
	
	public static final Zorganization fromJson(String json) {
		 return gson.fromJson(json, Zorganization.class);
	}

}
