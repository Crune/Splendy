package org.kh.splendy.vo;

import java.sql.Date;

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
}
