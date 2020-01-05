package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Account implements BlockObj {
    private String address;
    private int coin;
    private Map<String, String> exFunc;

    public Account(String address, int coin) {
        this();
        this.address = address;
        this.coin = coin;
    }

    public Account() {
        exFunc = new HashMap<>();
    }
}