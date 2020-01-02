package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.AdmitPacker;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class SummaryUtils {
    public static String gen(AdmitPacker admitPacker) {
        return new JSONObject()
            .put("verifier", admitPacker.getVerifier())
            .put("order", admitPacker.getOrder())
            .put("enter", admitPacker.getEnter())
            .put("hash", admitPacker.getPacker().getHash())
            .put("sign", admitPacker.getPacker().getSignMap())
            .toString();
    }
    
    public static String gen(BlockObj blockObj) {
        return genJson(blockObj).toString();
    }
    
    public static JSONObject genJson(BlockObj blockObj) {
        JSONObject json = new JSONObject();
        Class<?> c = blockObj.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            try {
                Class<?> type = field.getType();
                if (type.isPrimitive() || String.class.isAssignableFrom(type)) {
                    Optional.ofNullable(field.get(blockObj)).ifPresent(o -> {
                        field.setAccessible(true);
                        json.put(field.getName(), o);
                    });
                } else if (Map.class.isAssignableFrom(type)) {
                    Optional.ofNullable(field.get(blockObj)).ifPresent(o -> {
                        field.setAccessible(true);
                        json.put(field.getName(), (Map<?, ?>) o);
                    });
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
