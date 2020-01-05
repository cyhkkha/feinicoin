package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.PublicKeyHub;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("publicKeyHub")
public class MapPublicKeyHub extends ConcurrentHashMap<String, PublicKey> implements PublicKeyHub {
    @Override
    public PublicKeyHub setKey(String address, PublicKey publicKey) {
        put(address, publicKey);
        return this;
    }

    @Override
    public PublicKeyHub setKeySet(Map<String, PublicKey> keySet) {
        putAll(keySet);
        return this;
    }

    @Override
    public PublicKey getKey(String address) {
        return get(address);
    }

    @Override
    public PublicKey deleteKey(String address) {
        return remove(address);
    }
}
