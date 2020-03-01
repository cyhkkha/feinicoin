package name.feinimouse.thesis;

import lombok.Data;

import java.util.Map;

@Data

public class Transaction {
    long id;
    long timestamp;
    String type;
    
    String sender;
    String receiver;
    int value;
    
    long[] input;
    long[] output;
    String[] signatures;
    
    String[] contracts;
    Map<String, Object> exFunc;
}
