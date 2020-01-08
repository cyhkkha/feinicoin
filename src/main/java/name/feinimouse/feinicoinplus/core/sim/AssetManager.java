package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.data.Asset;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

public interface AssetManager {
    void genAsset(int number);
    boolean contain(String address);
    boolean contain(String address, String owner);
    boolean put(Asset asset);
    Asset get(String address, String owner);
    Asset getRandom();
    Packer[] getHistories(String address, String owner);
    int size();
    boolean remove(String address, String owner);

    PackerArr pack();
    boolean commit(Packer packer);
}
