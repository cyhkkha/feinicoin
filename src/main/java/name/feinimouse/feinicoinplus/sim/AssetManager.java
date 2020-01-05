package name.feinimouse.feinicoinplus.sim;

import name.feinimouse.feinicoinplus.core.block.Asset;

public interface AssetManager {
    void genAsset(int number);
    boolean contain(String address);
    boolean contain(String address, String owner);
    boolean put(Asset asset);
    Asset get(String address, String owner);
    Asset getRandom();
    Asset getRandomEx(Asset asset);
    int size();
    boolean remove(String address, String owner);
}
