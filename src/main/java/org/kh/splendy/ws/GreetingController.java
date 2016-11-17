package org.kh.splendy.ws;

import org.kh.splendy.service.StreamServiceImpl;
import org.kh.splendy.service.StreamServiceImpl.WSMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Controller
public class GreetingController {

	@Data
	public static class WSMsg {

		@SerializedName("type")
		@Expose
		public String type;
		@SerializedName("cont")
		@Expose
		public Object cont;

		public static WSMsg convert(String source) {
			return new Gson().fromJson(source, WSMsg.class);
		}
	}

	@MessageMapping("/msg") // 요청할 주소
	@SendTo("/topic/lobby") // 구독자가 연결된 주소
	public Greeting greeting(WSMsg message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getCont() + "!");
	}

}
