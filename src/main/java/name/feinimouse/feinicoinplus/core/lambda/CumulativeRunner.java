package name.feinimouse.feinicoinplus.core.lambda;

public interface CumulativeRunner<T> {
    T run(T accumulate, T pram);
}
