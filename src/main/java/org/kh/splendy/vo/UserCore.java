package org.kh.splendy.vo;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import org.kh.splendy.aop.SplendyAdvice;
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

	public int isSamePassword(String password) {		
		int login_result = -1;
		String check = null; 
		try {
			check = SplendyAdvice.getEncSHA256(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} if(this.password.equals(check)){
			login_result = 1;
		}
		return login_result;
	}	
	
	public void setPassword(String password) {		
		try {
			this.password = SplendyAdvice.getEncSHA256(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}	
	
}
