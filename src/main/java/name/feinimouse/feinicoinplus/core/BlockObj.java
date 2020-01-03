package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public interface BlockObj {
    default String genSummary() {
        return genJson().toString();
    }

    default JSONObject genJson() {
        JSONObject json = new JSONObject();
        Class<?> c = this.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            // PropIgnore 为不参加json化的标志
            if (field.getAnnotation(PropIgnore.class) != null) {
                continue;
            }
            Class<?> type = field.getType();
            field.setAccessible(true);
            try {
                if (Map.class.isAssignableFrom(type)) {
                    // Map 类型特殊处理
                    Optional.ofNullable(field.get(this)).ifPresent(o -> {
                        Map<?, ?> map = (Map<?, ?>) o;
                        if (!map.isEmpty()) {
                            json.put(field.getName(), map);
                        }
                    });
                } else if (BlockObj.class.isAssignableFrom(type)) {
                    // BlockObj 特殊处理
                    Optional.ofNullable(field.get(this)).ifPresent(o -> {
                        json.put(field.getName(), ((BlockObj) o).genJson());
                    });
                } else if (type.isPrimitive() || String.class.isAssignableFrom(type)) {
                    // 普通类型
                    Optional.ofNullable(field.get(this)).ifPresent(o -> {
                        json.put(field.getName(), o);
                    });
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
