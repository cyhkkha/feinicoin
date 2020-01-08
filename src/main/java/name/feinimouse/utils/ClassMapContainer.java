package name.feinimouse.utils;

import lombok.Getter;
import name.feinimouse.utils.exception.OverFlowException;
import name.feinimouse.utils.exception.UnrecognizedClassException;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ClassMapContainer<T> {
    private Map<Class<?>, Queue<T>> map;
    private Class<?>[] supportClass;

    // -1表示无限容量
    @Getter
    private int max = -1;
    private final AtomicInteger size;

    public ClassMapContainer(Class<?>[] supportClass) {
        this.supportClass = supportClass;
        size = new AtomicInteger(0);
        map = new ConcurrentHashMap<>();
        for (Class<?> cls : supportClass) {
            Queue<T> queue = new ConcurrentLinkedQueue<>();
            map.put(cls, queue);
        }
    }
    
    public ClassMapContainer(Class<?>[] supportClass, int max) {
        this(supportClass);
        setMax(max);
    }

    public synchronized boolean setMax(int newMax) {
        if (size.intValue() > newMax) {
            return false;
        }
        this.max = newMax;
        return true;
    }
    
    public synchronized void setMaxInfinitive() {
        this.max = -1;
    }

    public int size() {
        return size.intValue();
    }

    public boolean containClass(Class<?> c) {
        return map.containsKey(c);
    }

    // 注意：由于并发需要，不能仅仅凭借该方法的返回为真，就确定一定能取出元素，因取出元素后再次判断元素是否为空
    public boolean hasObject(Class<?> c) {
        return containClass(c) && map.get(c).size() > 0;
    }

    public abstract Class<?> getCoverClass(T t);
    
    public ClassMapContainer<T> put(T t) throws UnrecognizedClassException, OverFlowException {
        Class<?> c = getCoverClass(t);
        if (c == null || !containClass(c)) {
            throw new UnrecognizedClassException(c);
        }
        synchronized (size) {
            if (max >= 0 && size.intValue() >= max) {
                throw new OverFlowException("container overflow");
            }
            size.addAndGet(1);
        }
        map.get(c).add(t);
        return this;
    }

    public T poll(Class<?> c) {
        if (containClass(c)) {
            T t = map.get(c).poll();
            if (t != null) {
                synchronized (size) {
                    size.addAndGet(-1);
                }
            }
            return t;
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
