package org.kh.splendy.ws;

import org.kh.splendy.vo.WSMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


	@MessageMapping("/msg") // 요청할 주소
	@SendTo("/topic/lobby") // 구독자가 연결된 주소
	public Greeting greeting(WSMsg message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getCont() + "!");
	}

}
