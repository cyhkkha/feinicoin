package name.feinimouse.lambda;

public interface DiffCumulativeRunner<T, E> {
    E run(E accumulate, T pram);
}
