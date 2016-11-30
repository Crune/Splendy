package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.service.ChatService;
import org.kh.splendy.service.RoomService;
import org.kh.splendy.service.ServService;
import org.kh.splendy.service.SocketService;
import org.kh.splendy.service.UserInnerService;
import org.kh.splendy.service.UserProfileService;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.PropInDB;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.UserInner;
import org.kh.splendy.vo.UserProfile;
import org.kh.splendy.vo.WSMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 관리자 권한 및 관리 컨트롤러
 * @author jingyu
 *
 */
@Controller
public class AdminController {
	
	@Autowired private UserService userServ;
	
	@Autowired private ServService servServ;
	
	@Autowired private UserInnerService innerServ;
	
	@Autowired private ChatService chatServ;
	
	@Autowired private RoomService roomServ;
	
	@Autowired private SocketService sockServ;
	
	@Autowired private UserProfileService profServ;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/admin/index")
	public String index() {
		log.info("admin access index");
		return "admin/index";
	}
	
	
	@RequestMapping("/admin/servList")
	public String servList(Model model) throws Exception {
		log.info("admin access servicelist");
		List<PropInDB> list = servServ.readAll();
		model.addAttribute("list", list);
		return "admin/servList";
	}
	@RequestMapping(
			value = "/admin/servState",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int saveState(@ModelAttribute("servMF") PropInDB prop) throws Exception {
		int result = 0;
		if(prop.getValue() != "") {
			result = servServ.update(prop);
			log.info("admin save service : "+prop.getKey());
		}
		return result;
	}
	
	
	@RequestMapping("/admin/userList")
	public String userList(Model model) throws Exception {
		log.info("admin access userlist");
		List<UserCore> list = userServ.selectAll();
		model.addAttribute("list", list);
		return "admin/userList";
	}
	
	@RequestMapping("/admin/userState/{id}/")
	public @ResponseBody UserCore userState(@PathVariable int id) throws Exception {
		log.info("admin serch user Info : "+id);
		UserCore user = userServ.selectOne(id);
		return user;
	}
	@RequestMapping(
			value = "/admin/user_modify",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody void saveState(@ModelAttribute("modifyForm") UserCore user) {
		try {
			userServ.adminMF(user);
		} catch(Exception e) { e.printStackTrace(); }
		log.info("admin modify user info : "+user.getId());
	}
	
	
	@RequestMapping("/admin/userAuthority/{id}/")
	public @ResponseBody UserInner userAuthority(@PathVariable int id) throws Exception {
		log.info("admin serch user Authority : "+id);
		UserInner inner = innerServ.read(id);
		return inner;
	}
	@RequestMapping(
			value = "/admin/admin_modify",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody void saveAdmin(@ModelAttribute("adminMF") UserInner inner) throws Exception {
		int id = inner.getId();
		String role = inner.getRole();
		innerServ.setRole(id,role);
		log.info("admin modify authority : "+id+","+role);
	}
	
	
	@RequestMapping("/admin/userProfile/{id}/")
	public @ResponseBody UserProfile userProfile(@PathVariable int id) throws Exception {
		log.info("admin serch user Profile : "+id);
		UserProfile prof = profServ.read(id);
		return prof;
	}
	@RequestMapping(
			value = "/admin/user_profile",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody void saveProfile(@ModelAttribute("profMF") UserProfile prof) throws Exception {
		log.info("admin modify user profile : "+prof.getUserId());
		profServ.adminMF(prof);
	}
	
	
	@RequestMapping("/admin/deleteForm")
	public String adminDeleteForm(Model model){
		log.info("admin access delete-form");
		List<Msg> msg = chatServ.read_all();
		List<Room> room = roomServ.getCurrentRooms();
		model.addAttribute("msg", msg);
		model.addAttribute("room", room);
		return "admin/deleteForm";
	}
	@RequestMapping(
			value = "/admin/msg_delete",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int msgDelete(@RequestParam("mid") String[] mid) throws Exception {
		int result = 1;
		if(mid != null) {
			for(int i = 0; i < mid.length; i++ ){
				int id = new Integer(mid[i]);
				log.info("admin delete message : "+id);
				chatServ.delete(id);
				result = 0;
			}
		}
		return result;
	}
	@RequestMapping(
			value = "/admin/room_close",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int roomClose(@RequestParam("id") String[] id) throws Exception {
		int result = 1;
		if(id != null) {
			for(int i = 0; i < id.length; i++ ){
				int r_id = new Integer(id[i]);
				log.info("admin close room : "+id);
				roomServ.close(r_id);
				result = 0;
			}
		}
		return result;
	}
	
	
	@RequestMapping("/admin/notice")
	public String adminNotice(HttpSession session){
		log.info("admin access notice");
		UserCore user = (UserCore)session.getAttribute("user");
		session.setAttribute("user", user);
		return "admin/notice";
	}
	@RequestMapping(
			value = "/admin/notice_send",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int sendNotice(@RequestParam("content") String content,@RequestParam("nickname") String nickname) throws Exception {
		int result = 0;
		if(content != ""){
			log.info("admin send notice -> "+nickname+" : "+content);
			sockServ.send("/notice/everyone", new WSMsg(nickname, content));
			result = 1;
		} else { result = 0; }
		return result;
	}
}
