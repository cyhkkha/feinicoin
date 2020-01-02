package name.feinimouse.utils;

import name.feinimouse.feinicoinplus.core.lambda.OrdinaryRunner;
import name.feinimouse.feinicoinplus.core.lambda.ReturnRunner;

import java.util.Optional;

public class OpUtils<T> {
    private boolean ifNull;
    
    private OpUtils(boolean ifNull) {
        this.ifNull = ifNull;
    }
    public static <T> OpUtils<T> ofNull() {
        return new OpUtils<>(true);
    }
    public static <T> OpUtils<T> ofNotNull() {
        return new OpUtils<>(false);
    }
    public static <T> OpUtils<T> of(T t) {
        if (t == null) {
            return ofNull();
        }
        return ofNotNull();
    }
    public <E extends Exception> void throwEx(ReturnRunner<E> runner) throws E {
        if (ifNull) {
            throw runner.run();
        }
    }
    public void throwEx() throws Exception {
        if (ifNull) {
            throw new Exception();
        }
    }
    @SuppressWarnings("UnusedReturnValue")
    public OpUtils<T> ifNull(OrdinaryRunner runner) {
        if (ifNull) {
            runner.run();
        }
        return this;
    }
    
    public <N> OpUtils<N> ifNull(ReturnRunner<N> runner) {
        if (ifNull) {
            return of(runner.run());
        }
        return ofNotNull();
    }
    
    public <N> Optional<N> ifNullOption(ReturnRunner<N> runner) {
        if (ifNull) {
            return Optional.ofNullable(runner.run());
        }
        return Optional.empty();
    }
    
}

