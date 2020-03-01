package name.feinimouse.thesis;

import lombok.Data;

import java.security.PublicKey;
import java.util.Map;

@Data

public class Account {
    long id;
    long timestamp;
    long[] operates;

    String address;
    PublicKey publicKey;
    int value;
    
    Contract[] contracts;
    Map<String, Object> exFunc;
}
