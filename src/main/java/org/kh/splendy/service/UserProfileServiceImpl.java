package org.kh.splendy.service;

import javax.servlet.http.HttpSession;

import org.kh.splendy.mapper.UserProfileMapper;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class UserProfileServiceImpl implements UserProfileService{
	
	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

	@Autowired UserProfileMapper profMap;
	
	@Override
	public void updateIcon(String icon, HttpSession session) {
		int id = ((UserCore)session.getAttribute("user")).getId();
		profMap.setIcon(id, icon);		
	}

	@Override
	public String getIcon(HttpSession session) {
		int id = ((UserCore)session.getAttribute("user")).getId();
		String icon = profMap.getIcon(id);
		return icon;
	}
	
	@Override
	public void refreshUserProf(HttpSession session){
		int id = ((UserCore)session.getAttribute("user")).getId();
		UserProfile ups = profMap.read(id);
		session.setAttribute("profile", ups);		
	}
	
}
