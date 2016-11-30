package org.kh.splendy.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.mapper.UserMapper;
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
    @Autowired private UserMapper userMap;
	
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

	private static List<UserProfile> profList = null;
    private static Date profListTime = new Date(System.currentTimeMillis());

	@Override
	public List<UserProfile> getProfAll() {
        if (profList == null || (profListTime.getTime()) - (new Date(System.currentTimeMillis())).getTime() > 1000*60*60*24) {
            profList = profMap.getProfAll();
            for (int i = 0; i < profList.size(); i++) {
                int id = profList.get(i).getUserId();
                UserCore user = userMap.read(id);
                profList.get(i).setNickname(user.getNickname());
            }
        }
		return profList;
	}

	@Override
	public void updateRate(int id, int rate) {
		try{
			profMap.setRate(id, rate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public UserProfile read(int id) {
		UserProfile prof = profMap.read(id);
		return prof;
	}

	@Override
	public void adminMF(UserProfile prof) {
		profMap.adminMF(prof);
	}
	
}
