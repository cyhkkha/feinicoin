package name.feinimouse.feinicoinplus.core.sim;

import name.feinimouse.feinicoinplus.core.data.SimResult;

public interface ResultManager {
    void newResult(String name);

    void clearAll();

    void put(String x, String y);

    void put(SimResult result);

    SimResult get(String name);

    String[] getResultNames();
}
