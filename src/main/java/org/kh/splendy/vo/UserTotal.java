package org.kh.splendy.vo;

import lombok.Data;

@Data
public class UserTotal {
	
	private UserCore user;
	private UserInner inner;
	private UserProfile prof;
	private Player pl;
	private Room room;

}
