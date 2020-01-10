package name.feinimouse.utils;

public interface StopwatchResult<T> {
    T get();

    long getTotalRunTime();

    long[] getRunTimes();
}
