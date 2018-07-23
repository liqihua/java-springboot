package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysUser extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String locked;
	private String gender;
	private String nickname;
	
	private String roleId;
	
	public SysUser() {
		super();
	}
	
	public SysUser(String username, String password, String locked,
			String gender, String nickname) {
		super();
		this.username = username;
		this.password = password;
		this.locked = locked;
		this.gender = gender;
		this.nickname = nickname;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "SysUser [username=" + username + ", password=" + password
				+ ", locked=" + locked + ", gender=" + gender + ", nickname="
				+ nickname + "]";
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}
