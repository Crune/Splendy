package org.kh.splendy.assist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import lombok.*;
import org.kh.splendy.mapper.*;
import org.kh.splendy.protocol.RoomProtocol;
import org.kh.splendy.service.*;
import org.kh.splendy.trash.StreamService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.google.gson.Gson;


@WSController
public abstract class ProtocolHelper {
	
	@Autowired protected StreamService stream;

	@Autowired protected RoomMapper roomMap;
	@Autowired protected MsgMapper msgMap;
	
	@Autowired protected PlayerMapper playerMap;
	
	@Autowired protected UserInnerMapper innerMap;
	@Autowired protected UserMapper userMap;
	@Autowired protected UserProfileMapper profMap;
	
	@Autowired private SimpMessagingTemplate simpT;

	private Logger log = LoggerFactory.getLogger(ProtocolHelper.class);
	
	public class WSession {
		@Getter private String id;
		private Map<String, Object> attributes;
		public Object getAttributes(String name) {
			return attributes.get(name);
		}
		public void setAttributes(String name, Object obj) {
			attributes.put(name, obj);
		}
		
		public WSession(SimpMessageHeaderAccessor headerAccessor) {
			id = headerAccessor.getSessionId();
			attributes = headerAccessor.getSessionAttributes();
		}
	}

	public static MessageHeaders createHeaders(String sessionId, SimpMessageType type) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(type);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

	public void kick(String sessionId, String channel) {
		simpT.convertAndSendToUser(sessionId, channel, "kicked!", ProtocolHelper.createHeaders(sessionId, SimpMessageType.DISCONNECT));
	}
	
	public UserCore sender(SimpMessageHeaderAccessor head) {
		return (UserCore) head.getSessionAttributes().get("user");
	}
	
	public void send(String type, Object obj) {
		if (type != null) {
			if (obj == null) {
				simpT.convertAndSend(type , "");
				log.info("send_msg: "+type);
			} else {
				simpT.convertAndSend(type , obj);
				log.info("send_msg: "+type+"/"+obj);
			}
		}
	}

	/** 현재 시간을 문자열로 반환
	 * @return 현재 시간의 HH:mm:ss 형식의 문자열 반환 */
	public static String getCurrentTime() {
		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		return df.format(new Date());
	}

	public static Object copy(Object obj)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = null;
		if (obj != null) {
			result = obj.getClass().newInstance();
			inject(result, obj);
		}
		return result;
	}
	
    public static void inject(Object target, Object org) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method[] methods = target.getClass().getMethods();
        
        Map<String, Method> getter = new HashMap<String, Method>();
        Map<String, Method> setter = new HashMap<String, Method>();
        
        for (Method m : methods) {
            if (m.getName().length()>3) {
                String head = m.getName().substring(0, 3);
                String name = m.getName().substring(3);
                if (head.equals("set")) setter.put(name, m);
                if (head.equals("get")) getter.put(name, m);
            }
        }
        
        for (String curMethodName : setter.keySet()) {
            if (getter.containsKey(curMethodName)) {
                Object data = getter.get(curMethodName).invoke(org);
                setter.get(curMethodName).invoke(target,data);
            }
        }
    }
}
