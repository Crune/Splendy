package org.kh.splendy.vo;

import lombok.Data;

@Data
public class UserInner {
	
	private int id;
	private String wsSession;
	private String wsAuthCode;
	private String regCode;
	private int connect;
	private String role;
	private String was;

}
