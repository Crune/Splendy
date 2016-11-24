package org.kh.splendy.service;

import javax.servlet.http.HttpSession;

import org.kh.splendy.vo.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;


public interface UserProfileService {

	void updateIcon(String icon, HttpSession session);
	String getIcon(HttpSession session);
	void refreshUserProf(HttpSession session);
	
}
