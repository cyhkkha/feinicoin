package name.feinimouse.feinicoinplus.core;

import java.security.PublicKey;
import java.util.Map;
import java.util.Set;

public interface PublicKeyHub {
    
    PublicKeyHub setKey(String address, PublicKey publicKey);
    
    PublicKeyHub setKeySet(Map<String, PublicKey> keySet);
    
    PublicKey getKey(String address);
    
    PublicKey deleteKey(String address);
    
    Set<String> addressSet();
    
}
