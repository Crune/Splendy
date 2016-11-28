package org.kh.splendy.config.assist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by runec on 2016-11-27.
 */
public class Utils {

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

        Map<String, Method> getter = new HashMap<>();
        Map<String, Method> setter = new HashMap<>();

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
