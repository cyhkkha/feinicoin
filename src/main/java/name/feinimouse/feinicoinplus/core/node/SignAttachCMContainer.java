package name.feinimouse.feinicoinplus.core.node;

import lombok.Getter;
import name.feinimouse.feinicoinplus.core.node.exce.InconsistentClassException;
import name.feinimouse.feinicoinplus.core.node.exce.OverFlowException;
import name.feinimouse.feinicoinplus.core.node.exce.UnrecognizedClassException;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SignAttachCMContainer {
    private Map<Class<?>, Queue<SignAttachObj<?>>> map;
    private Class<?>[] supportClass;
    
    @Getter
    private int max = 20;
    private final AtomicInteger size;
    
    public SignAttachCMContainer(Class<?>[] supportClass) {
        this.supportClass = supportClass;
        size = new AtomicInteger(0);
        map = new ConcurrentHashMap<>();
        for (Class<?> cls : supportClass) {
            Queue<SignAttachObj<?>> queue = new ConcurrentLinkedQueue<>();
            map.put(cls, queue);
        }
    }

    public synchronized boolean setMax(int max) {
        if (size.intValue() > max) {
            return false;
        }
        this.max = max;
        return true;
    }
    
    public int size() {
        return size.intValue();
    }
    
    public boolean containClass(Class<?> c) {
        return map.containsKey(c);
    }
    
    public boolean hasObject(Class<?> c) {
        return containClass(c) && map.get(c).size() > 0;
    }
    
    public SignAttachCMContainer put(Class<?> c, SignAttachObj<?> signAttachObj) throws UnrecognizedClassException, InconsistentClassException, OverFlowException {
        if (!containClass(c)) {
            throw new UnrecognizedClassException(c);
        }
        if (!c.equals(signAttachObj.getObj().obj().getClass())) {
            throw new InconsistentClassException(c);
        }
        synchronized (size) {
            if (size.intValue() >= max) {
                throw new OverFlowException("NSAOMCQueue overflow");
            }
            size.addAndGet(1);
        }
            map.get(c).add(signAttachObj);
            return this;
    }
    
    @SuppressWarnings("unchecked")
    public <T> SignAttachObj<T> get(Class<T> c) {
        if (containClass(c)) {
            SignAttachObj<T> signAttachObj = (SignAttachObj<T>) map.get(c).poll();
            if (signAttachObj != null) {
                synchronized (size) {
                    size.addAndGet(-1);
                }
            }
            return signAttachObj;
        }
        return null;
    } 
    
    public Class<?>[] getSupportClass() {
        return supportClass.clone();
    }
    
    public void clear() {
        map.values().forEach(Collection::clear);
        map.clear();
    }
}
