package name.feinimouse.feinicoinplus.sim;

import name.feinimouse.feinicoinplus.core.block.Asset;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AssetManager {

    private Map<String, Map<String, Asset>> assetAddressMap;
    private Map<String, Map<String, Asset>> userAssetMap;

    private AddressManager addressManager;
    private AccountManager accountManager;

    private Random random = new Random();

    private int size = 0;

    public AssetManager(AddressManager addressManager, AccountManager accountManager) {
        assetAddressMap = new ConcurrentHashMap<>();
        userAssetMap = new ConcurrentHashMap<>();
        this.addressManager = addressManager;
        this.accountManager = accountManager;
    }

    public void genAsset(int number) {
        for (int i = 0; i < number; i++) {
            String address = addressManager.getAddress();
            int coin = random.nextInt(100) + 500;
            String owner = accountManager.getRanAccount().getAddress();
            Asset asset = new Asset(address, owner, coin);
            putAsset(asset);
        }
    }

    public boolean containAsset(String address) {
        return assetAddressMap.containsKey(address);
    }

    public boolean containAsset(String address, String owner) {
        if (containAsset(address)) {
            return assetAddressMap.get(address).containsKey(owner);
        }
        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public synchronized boolean putAsset(Asset asset) {
        String address = asset.getAddress();
        String owner = asset.getOwner();
        if (!accountManager.containAccount(owner)) {
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
    
    public Asset getRanAsset() {
        Map<String, Asset> userMap = getRandomFromMap(assetAddressMap);
        if (userMap != null) {
            return getRandomFromMap(userMap);
        }
        return null;
    }


    public Asset getAsset(String address, String owner) {
        return assetAddressMap.get(address).get(owner);
    }

    public int size() {
        return size;
    }

    public Asset getRanAssetEx(Asset asset) {
        Asset assetNext = getRanAsset();
        while (asset == assetNext) {
            assetNext = getRanAsset();
        }
        return assetNext;
    }

    public synchronized boolean removeAsset(String address, String owner) {
        if (containAsset(address)) {
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

}
