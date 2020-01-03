package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.*;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public interface HashGen {
    String hash(String content);
    Packer hash(BlockObj blockObj);
    AdmitPackerArr hash(BlockObj[] objArr, Class<?> aClass);
    AdmitPackerArr hash(Packer[] objArr, Class<?> aClass);
}
