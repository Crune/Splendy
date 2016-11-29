package org.kh.splendy.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.vo.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;


public interface UserProfileService {

	void updateIcon(String icon, HttpSession session);
	String getIcon(HttpSession session);
	void refreshUserProf(HttpSession session);
	List<UserProfile> getProfAll();
	void updateRate(int id, int rate);
	UserProfile read(int id);
	void adminMF(UserProfile prof);
	
}
