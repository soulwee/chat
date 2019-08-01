package com.seecen.entity;

public class Group {
	private int groupId;
	private String groupName;
	private int userId;
	
	private int count;//总人数
	private int zxcount;//在线人数
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getZxcount() {
		return zxcount;
	}
	public void setZxcount(int zxcount) {
		this.zxcount = zxcount;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
