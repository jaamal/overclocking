package compressionservice.handlers.binding;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class Binder {
    public static <T extends Enum<T>> T getEnum(HttpServletRequest request, Class<T> enumClass, String key) {
        String str = request.getParameter(key);
        if (str == null || str == "")
            throw new ParameterBindException(String.format("Parameter %s is required but was not passed.", key));
        return Enum.valueOf(enumClass, str);
    }

    public static UUID getUUID(HttpServletRequest request, String key, UUID defaultValue) {
        String str = request.getParameter(key);
        if (str == null || str == "")
            return defaultValue;
        
        try {
            return UUID.fromString(str);
        }
        catch (IllegalArgumentException e) {
            throw new ParameterBindException(String.format("Parameter %s has invalid format %s.", key, str), e);
        }
    }
    
    public static int getInt(HttpServletRequest request, String key, int defaultValue) {
        String str = request.getParameter(key);
        return (str == null || str == "") 
                ? defaultValue 
                : Integer.parseInt(str);
    }
    
    public static Map<String, String> getAllParams(HttpServletRequest request) {
        HashMap<String, String> result = new HashMap<>();
        
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            result.put(name, request.getParameter(name));
        }
        return result;
    }
}
