package name.feinimouse.thesis;

import lombok.Data;

import java.util.Map;

@Data

public class Asset {
    long id;
    long timestamp;
    long operate;
    
    String type;
    String owner;
    String issuer;
    int value;

    Contract[] contracts;
    Map<String, Object> exFunc;
}
