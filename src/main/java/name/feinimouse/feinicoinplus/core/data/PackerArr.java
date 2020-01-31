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

public class PackerArr extends MapSignHashCover {
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

    public int size() {
        return arr.length;
    }

    @Override
    public PackerArr clone() {
        PackerArr result = (PackerArr) super.clone();
        result.arr = arr.clone();
        return result;
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
