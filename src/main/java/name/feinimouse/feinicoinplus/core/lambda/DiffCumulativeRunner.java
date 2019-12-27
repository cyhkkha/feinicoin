package name.feinimouse.feinicoinplus.core.lambda;

public interface DiffCumulativeRunner<T, E> {
    E run(E accumulate, T pram);
}
