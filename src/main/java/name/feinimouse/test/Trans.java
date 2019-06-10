package name.feinimouse.test;

import lombok.Data;

@Data
public class Trans {
    private String sender;
    private String reciever;
    private int coin;
    private String sign;
}
