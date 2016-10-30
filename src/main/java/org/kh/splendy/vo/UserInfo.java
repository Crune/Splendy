package org.kh.splendy.vo;

import java.sql.Date;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

public class UserInfo extends User {

	private int id;
	private String nickname;
	private String email;
	private String password;
	private int enabled;
	private int notLocked;
	private int notExpired;
	private int notCredential;
	private Date reg;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public int getNotLocked() {
		return notLocked;
	}
	public void setNotLocked(int notLocked) {
		this.notLocked = notLocked;
	}
	public int getNotExpired() {
		return notExpired;
	}
	public void setNotExpired(int notExpired) {
		this.notExpired = notExpired;
	}
	public int getNotCredential() {
		return notCredential;
	}
	public void setNotCredential(int notCredential) {
		this.notCredential = notCredential;
	}
	public Date getReg() {
		return reg;
	}
	public void setReg(Date reg) {
		this.reg = reg;
	}
	
}
