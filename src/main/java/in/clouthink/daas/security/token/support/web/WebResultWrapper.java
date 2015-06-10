package in.clouthink.daas.security.token.support.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class WebResultWrapper {
    
    public static Map<String, Object> succeedMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.TRUE);
        return result;
    }
    
    public static Map<String, Object> succeedMap(Object obj) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.TRUE);
        if (null != obj) {
            result.put("data", obj);
        }
        return result;
    }
    
    public static Map<String, Object> succeedMap(String key, Object value) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(key, value);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.TRUE);
        result.put("data", data);
        return result;
    }
    
    public static Map<String, Object> failedMap() {
        return failedMap("");
    }
    
    public static boolean isFailedMap(Map<String, Object> map) {
        return (map.get("succeed") == Boolean.FALSE);
    }
    
    public static String getFailedMessage(Map<String, Object> map) {
        return (String) map.get("message");
    }
    
    public static String[] getFailedMessages(Map<String, Object> map) {
        Object messages = map.get("messages");
        if (messages == null) {
            return null;
        }
        if (messages instanceof String[]) {
            return (String[]) messages;
        }
        else if (messages instanceof List) {
            return ((List<String>) map.get("messages")).toArray(new String[0]);
        }
        else {
            return null;
        }
    }
    
    public static Map<String, Object> failedMap(int errorCode, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("errorCode", errorCode);
        result.put("message", message);
        return result;
    }
    
    public static Map<String, Object> failedMap(String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("message", message);
        return result;
    }
    
    public static Map<String, Object> failedMap(int errorCode, String[] messages) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("errorCode", errorCode);
        result.put("messages", messages);
        return result;
    }
    
    public static Map<String, Object> failedMap(String[] messages) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("messages", messages);
        return result;
    }
    
    public static Map<String, Object> failedMap(int errorCode,
                                                List<String> messages) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("errorCode", errorCode);
        result.put("messages", messages);
        return result;
    }
    
    public static Map<String, Object> failedMap(List<String> messages) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("messages", messages);
        return result;
    }
    
    public static Map<String, Object> failedMap(String message,
                                                String code,
                                                String field) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("succeed", Boolean.FALSE);
        result.put("message", message);
        Map<String, Object> errorDetail = new HashMap<String, Object>();
        errorDetail.put("code", code);
        errorDetail.put("field", field);
        result.put("error", errorDetail);
        return result;
    }
}
