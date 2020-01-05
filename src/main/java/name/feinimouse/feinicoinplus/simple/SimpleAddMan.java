package name.feinimouse.feinicoinplus.simple;

import name.feinimouse.feinicoinplus.sim.AddressManager;
import name.feinimouse.utils.HexUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleAddMan implements AddressManager {
    private ConcurrentHashMap<String, Object> addressSet;
    private ConcurrentLinkedQueue<String> waitUse;
    private ConcurrentHashMap<String, Object> using;

    private static final Object PRESENT = new Object();

    public SimpleAddMan() {
        addressSet = new ConcurrentHashMap<>();
    }

    @Override
    public void genAddress(int number) {
        for (int i = 0; i < number; i ++) {
            String address = HexUtils.randomHexString(16);
            while (contains(address)) {
                address = HexUtils.randomHexString(16);
            }
            put(address);
        }
    }

    @Override
    public boolean put(String address) {
        if (addressSet.containsKey(address)) {
            return false;
        }
        addressSet.put(address, PRESENT);
        waitUse.add(address);
        return true;
    }

    @Override
    public boolean contains(String address) {
        return addressSet.containsKey(address);
    }

    @Override
    public String remove(String address) {
        if (addressSet.containsKey(address)) {
            addressSet.remove(address);
            return address;
        }
        return null;
    }

    @Override
    public void returnAddress(String address) {
        if (using.containsKey(address)) {
            using.remove(address);
            waitUse.add(address);
            return;
        }
        put(address);
    }

    @Override
    public String getAddress() {
        if (retainSize() <= 0) {
            genAddress(100);
        }
        String address = waitUse.poll();
        if (address != null) {
            using.put(address, PRESENT);
        }
        return address;
    }

    @Override
    public int size() {
        return addressSet.size();
    }

    @Override
    public int usingSize() {
        return using.size();
    }

    @Override
    public int retainSize() {
        return waitUse.size();
    }
}
