package org.kh.splendy.controller;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 로그인 후 프로필 정보 전달
 * 
 * @author 민정
 *
 */

@Controller
@RequestMapping("/main/facebook")
public class FaceController {
	private Facebook facebook;
	private ConnectionRepository connectionRepository;

	public FaceController(Facebook facebook, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
	}

	@GetMapping
	public String helloFacebook(Model model) throws Exception {
		if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
			return "redirect:/connect/facebook";
		}

		model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
		
		System.out.println(facebook.userOperations().getUserProfile().getId());
		/*
		 * PagedList<Post> feed = facebook.feedOperations().getFeed();
		 * model.addAttribute("feed", feed);
		 */
		return "user/hello";
	}
}