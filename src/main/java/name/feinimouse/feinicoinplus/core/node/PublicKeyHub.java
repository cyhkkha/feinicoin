package name.feinimouse.feinicoinplus.core.node;

import java.security.PublicKey;
import java.util.concurrent.ConcurrentHashMap;

public class PublicKeyHub {
    private ConcurrentHashMap<String, PublicKey> keyMap;
    
    public PublicKeyHub() {
        keyMap = new ConcurrentHashMap<>();
    }
    
    public PublicKeyHub setKey(String address, PublicKey publicKey) {
        keyMap.put(address, publicKey);
        return this;
    }
    
    public PublicKey getKey(String address) {
        return keyMap.get(address);
    }
    
    public PublicKey deleteKey(String address) {
        return keyMap.remove(address);
    }
    
}
