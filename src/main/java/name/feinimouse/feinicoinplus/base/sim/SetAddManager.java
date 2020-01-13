package name.feinimouse.feinicoinplus.base.sim;

import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.utils.HexUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component("addressManager")
public class SetAddManager implements AddressManager, InitializingBean {
    private ConcurrentHashMap<String, Object> addressSet;
    private ConcurrentLinkedQueue<String> waitUse;
    private ConcurrentHashMap<String, Object> using;

    private static final Object PRESENT = new Object();

    @Value("${NUMBER_ADDRESS}")
    private int NUMBER_ADDRESS;

    public SetAddManager() {
        addressSet = new ConcurrentHashMap<>();
        waitUse = new ConcurrentLinkedQueue<>();
        using = new ConcurrentHashMap<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        genAddress(NUMBER_ADDRESS);
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
