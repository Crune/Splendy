package org.kh.splendy.vo;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import org.kh.splendy.config.aop.SplendyAdvice;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.Data;

@SessionAttributes
@Data
public class UserCore {
	
	private int id;
	private String nickname;
	private String email;
	private String password;
	private int enabled;
	private int notLocked;
	private int notExpired;
	private int notCredential;
	private Date reg;

	public boolean isSamePassword(String password) {		
		boolean login_result = false;
		String check = null; 
		try {
			check = SplendyAdvice.getEncSHA256(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} if(this.password.equals(check)){
			login_result = true;
		}
		return login_result;
	}	
	
	public void setPassword(String password) {		
		if(password != null) {
			if(!password.isEmpty()){
				try {
					this.password = SplendyAdvice.getEncSHA256(password);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	public void openInfo() {
		this.password = "";
		this.enabled = 0;
		this.notLocked = 0;
		this.notExpired = 0;
		this.notCredential = 0;
		this.reg = new Date(0);
	}
}
