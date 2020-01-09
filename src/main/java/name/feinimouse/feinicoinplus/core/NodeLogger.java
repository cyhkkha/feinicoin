package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.Carrier;
import org.aspectj.lang.JoinPoint;

public interface NodeLogger {
    void afterCommit(JoinPoint joinPoint, Carrier carrier);

    void afterFetch(JoinPoint joinPoint, Carrier carrier, Carrier ret);

    void beforeStop(JoinPoint joinPoint);
}
