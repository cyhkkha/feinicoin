package name.feinimouse.feinicoinplus.sim;

import name.feinimouse.utils.HexUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AddressManager {
    private ConcurrentHashMap<String, Object> addressSet;
    private ConcurrentLinkedQueue<String> waitUse;
    private ConcurrentHashMap<String, Object> using;

    private static final Object PRESENT = new Object();

    public AddressManager() {
        addressSet = new ConcurrentHashMap<>();
    }
    
    public void expand(int number) {
        for (int i = 0; i < number; i ++) {
            String address = HexUtils.randomHexString(16);
            while (contains(address)) {
                address = HexUtils.randomHexString(16);
            }
            put(address);
        }
    }

    public boolean put(String address) {
        if (addressSet.containsKey(address)) {
            return false;
        }
        addressSet.put(address, PRESENT);
        waitUse.add(address);
        return true;
    }

    public boolean contains(String address) {
        return addressSet.containsKey(address);
    }

    public String remove(String address) {
        if (addressSet.containsKey(address)) {
            addressSet.remove(address);
            return address;
        }
        return null;
    }

    public void returnAddress(String address) {
        if (using.containsKey(address)) {
            using.remove(address);
            waitUse.add(address);
            return;
        }
        put(address);
    }
    
    public String getAddress() {
        if (retainSize() <= 0) {
            expand(100);
        }
        String address = waitUse.poll();
        if (address != null) {
            using.put(address, PRESENT);
        }
        return address;
    }
    
    public int size() {
        return addressSet.size();
    }

    public int usingSize() {
        return using.size();
    }

    public int retainSize() {
        return waitUse.size();
    }
}
