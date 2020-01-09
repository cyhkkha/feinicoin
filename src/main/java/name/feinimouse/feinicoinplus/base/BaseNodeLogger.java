package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.NodeLogger;
import name.feinimouse.feinicoinplus.core.data.Carrier;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class BaseNodeLogger implements NodeLogger {
    @Pointcut("execution(* name.feinimouse.feinicoinplus.core.node.Node.commit(..))")
    public void commit(){}

    @Pointcut("execution(* name.feinimouse.feinicoinplus.core.node.Node.fetch(..))")
    public void fetch(){}

    @Pointcut("execution(* name.feinimouse.feinicoinplus.core.node.Node.stopNode(..))")
    public void stopNode(){}

    @After("commit() && args(carrier)")
    public void afterCommit(JoinPoint joinPoint, Carrier carrier) {

    }

    @AfterReturning(pointcut = "fetch() && args(carrier)", returning = "ret", argNames = "joinPoint,carrier,ret")
    public void afterFetch(JoinPoint joinPoint, Carrier carrier, Carrier ret) {

    }

    @Before("stopNode()")
    public void beforeStop(JoinPoint joinPoint) {

    }
}
