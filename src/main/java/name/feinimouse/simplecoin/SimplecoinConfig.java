package name.feinimouse.simplecoin;

import java.util.Arrays;

public class SimplecoinConfig {
    public Integer[] TEST_COUNT;
    public int BUNDLE_SIZE;
    public int UTXO_SIZE;
    public double ASSET_RATE;
    public int USER_COUNT;
    
    public boolean baseInfo = false;
    public boolean account = false;
    public boolean utxo = false;
    public boolean bcbdc = false;
    public boolean clean = false;
    
    public String output;
    public int CYCLE_TIME;
    
    public SimplecoinConfig() {}

    public SimplecoinConfig(int USER_COUNT) {
        this.USER_COUNT = USER_COUNT;
    }

    public SimplecoinConfig(int USER_COUNT, Integer[] TEST_COUNT, int BUNDLE_SIZE, int UTXO_SIZE, double ASSET_RATE) {
        this.TEST_COUNT = TEST_COUNT;
        this.BUNDLE_SIZE = BUNDLE_SIZE;
        this.UTXO_SIZE = UTXO_SIZE;
        this.ASSET_RATE = ASSET_RATE;
        this.USER_COUNT = USER_COUNT;
    }
    
    @Override
    public String toString() {
        return "TEST_COUNT: " + Arrays.toString(TEST_COUNT) + "\n" +
            "USER_COUNT: " + USER_COUNT + "\n" +
            "BUNDLE_SIZE: " + BUNDLE_SIZE + "\n" +
            "UTXO_SIZE: " + UTXO_SIZE + "\n" +
            "ASSET_RATE: " + ASSET_RATE + "\n" +
            "CYCLE_TIME: " + CYCLE_TIME + "\n" +
            "OUTPUT: " + output + "\n";
    }
}
