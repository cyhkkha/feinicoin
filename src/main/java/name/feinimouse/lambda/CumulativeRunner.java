package name.feinimouse.lambda;

public interface CumulativeRunner<T> {
    T run(T accumulate, T pram);
}
