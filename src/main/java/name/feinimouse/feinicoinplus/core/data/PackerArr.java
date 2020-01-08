package name.feinimouse.feinicoinplus.core.data;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PackerArr extends MapSignObj implements Cloneable {
    @Getter
    @Setter
    private String hash;
    
    @PropIgnore
    @Setter
    private Packer[] arr;
    
    @PropIgnore
    @Setter
    private Class<?> objClass;
    
    public PackerArr(Packer[] arr, Class<?> objClass) {
        this.arr = arr;
        this.objClass = objClass;
    }
    public PackerArr(String hash, Packer[] arr, Class<?> objClass) {
        this(arr, objClass);
        this.hash = hash;
    }

    public PackerArr copy() {
        try {
            PackerArr result = (PackerArr) clone();
            result.setArr(arr.clone());
            // map的克隆
            Optional.ofNullable(sign).ifPresent(sign -> {
                Class<?> mapClass = sign.getClass();
                Map<String, String> map = null;
                try { // 使用原有的map类型来克隆
                    Constructor<?> con = mapClass.getConstructor(Map.class);
                    if (con != null) {
                        //noinspection unchecked
                        map = (Map<String, String>) con.newInstance(sign);
                        result.setSign(map);
                    }
                } catch (ClassCastException | NoSuchMethodException 
                    | IllegalAccessException | InstantiationException 
                    | InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (map == null) {
                    map = new ConcurrentHashMap<>(sign);
                }
                result.setSign(map);
            });
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object obj() {
        return arr;
    }

    @Override
    public Class<?> objClass() {
        return objClass;
    }

    @Override
    public JSONObject genJson() {
        JSONArray array = new JSONArray();
        for (Packer packer : arr) {
            array.put(packer.genJson());
        }
        return super.genJson().put("obj", array);
    }
}
