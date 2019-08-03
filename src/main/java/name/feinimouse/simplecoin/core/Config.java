package name.feinimouse.simplecoin.core;

import lombok.Data;

@Data
public class Config {
    private Integer[] TEST_COUNT;
    private int BUNDLE_SIZE;
    private int UTXO_SIZE;
    private double ASSET_RATE;
    private int USER_COUNT;
    
    public Config() {}

    public Config(int USER_COUNT) {
        this.USER_COUNT = USER_COUNT;
    }

    public Config(int USER_COUNT, Integer[] TEST_COUNT, int BUNDLE_SIZE, int UTXO_SIZE, double ASSET_RATE) {
        this.TEST_COUNT = TEST_COUNT;
        this.BUNDLE_SIZE = BUNDLE_SIZE;
        this.UTXO_SIZE = UTXO_SIZE;
        this.ASSET_RATE = ASSET_RATE;
        this.USER_COUNT = USER_COUNT;
    }
}
