package org.kh.splendy.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleChatController {

	@RequestMapping("/chat/")
	public String chat() {
		return "chat";
	}
}
