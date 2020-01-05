package name.feinimouse.feinicoinplus.core.block;

import lombok.Data;
import name.feinimouse.feinicoinplus.core.BlockObj;

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