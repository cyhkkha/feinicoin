package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component("assetManager")
public class MapAssManager implements AssetManager {
    private HashGenerator hashGenerator;
    private AddressManager addressManager;
    private AccountManager accountManager;
    
    private Map<String, Map<String, Asset>> assetAddressMap;
    private Map<String, Map<String, Asset>> userAssetMap;

    private Random random = new Random();

    private int size = 0;

    @Autowired
    public MapAssManager(HashGenerator hashGenerator, AddressManager addressManager, AccountManager accountManager) {
        assetAddressMap = new ConcurrentHashMap<>();
        userAssetMap = new ConcurrentHashMap<>();
        this.addressManager = addressManager;
        this.accountManager = accountManager;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public void genAsset(int number) {
        for (int i = 0; i < number; i++) {
            Account account = accountManager.getRandom();
            if (account == null) {
                break;
            }
            String owner = account.getAddress();
            String address = addressManager.getAddress();
            int coin = random.nextInt(100) + 1000;
            Asset asset = new Asset(address, owner, coin);
            put(asset);
        }
    }
    @Override
    public boolean contain(String address) {
        return assetAddressMap.containsKey(address);
    }
    @Override
    public boolean contain(String address, String owner) {
        if (contain(address)) {
            return assetAddressMap.get(address).containsKey(owner);
        }
        return false;
    }

    @Override
    public synchronized boolean put(Asset asset) {
        String address = asset.getAddress();
        String owner = asset.getOwner();
        if (!accountManager.contain(owner)) {
            return false;
        }

        if (userAssetMap.containsKey(owner) || assetAddressMap.containsKey(address)) {
            return false;
        }

        Map<String, Asset> userMap = userAssetMap.get(owner);
        if (userMap == null) {
            userMap = new ConcurrentHashMap<>();
            userAssetMap.put(owner, userMap);
        }
        userMap.put(address, asset);

        Map<String, Asset> addressMap = assetAddressMap.get(address);
        if (addressMap == null) {
            addressMap = new ConcurrentHashMap<>();
            assetAddressMap.put(address, addressMap);
        }
        addressMap.put(owner, asset);

        size++;
        return true;
    }
    
    private <T> T getRandomFromMap(Map<?, T> map) {
        if (map.size() <= 0) {
            return null;
        }
        int index = random.nextInt(map.size());
        Object key = map.keySet().toArray()[index];
        return map.get(key);
    }

    public Asset getRandom() {
        Map<String, Asset> userMap = getRandomFromMap(assetAddressMap);
        if (userMap != null) {
            return getRandomFromMap(userMap);
        }
        return null;
    }

    @Override
    public Asset get(String address, String owner) {
        return assetAddressMap.get(address).get(owner);
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public Asset getRandomEx(Asset asset) {
        Asset assetNext = getRandom();
        while (asset == assetNext) {
            assetNext = getRandom();
        }
        return assetNext;
    }
    @Override
    public synchronized boolean remove(String address, String owner) {
        if (contain(address)) {
            Map<String, Asset> map = assetAddressMap.get(address);
            if (map.containsKey(owner)) {
                map.remove(owner);
                if (map.size() <= 0) {
                    assetAddressMap.remove(address);
                }
                map = userAssetMap.get(owner);
                map.remove(address);
                if (map.size() <= 0) {
                    userAssetMap.remove(owner);
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public PackerArr pack() {
        LinkedList<Asset> assets = new LinkedList<>();
        for (String address : assetAddressMap.keySet()) {
            Map<String, Asset> map = assetAddressMap.get(address);
            for (String user : map.keySet()) {
                assets.add(map.get(user));
            }
        }
        return hashGenerator.hash(assets.toArray(Asset[]::new), Asset.class);
    }

    @Override
    public synchronized boolean commit(AssetTrans assetTrans) {
        return false;
    }
}
