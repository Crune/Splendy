package org.kh.splendy.config.ws;

import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class SplendyWSHandshakeInterceptor implements HandshakeInterceptor {


    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(SplendyWSHandshakeInterceptor.class);


    @Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
            HttpHeaders header = request.getHeaders();

            if (header != null) {
				/*
                log.info("헤더에 이게 들어감"+header);
                log.info("plServ: "+playerService);
                int rid = playerService.getLastRoomAndInit(user.getId());

                if (rid != reqRid) {
                    if (playerService.join(user.getId(), reqRid, "")) {
                        rid = reqRid;
                    }
                }*/
            }
            if (session != null) {
				UserCore user = (UserCore) session.getAttribute("user");
                int reqRid = (int) session.getAttribute("rid");

				attributes.put("user", user);
				attributes.put("uid", user.getId());
				attributes.put("rid", reqRid);
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) { }

}
