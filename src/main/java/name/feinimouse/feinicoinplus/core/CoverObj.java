package name.feinimouse.feinicoinplus.core;

import org.json.JSONObject;

public interface CoverObj<T> {
    JSONObject getCover();
    T obj();
    Class<T> objClass();
}
