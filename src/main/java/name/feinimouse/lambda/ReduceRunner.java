package name.feinimouse.lambda;

public interface ReduceRunner<T, E> {
    E run(E accumulate, T pram);
}
