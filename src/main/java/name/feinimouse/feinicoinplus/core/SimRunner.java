package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;

public interface SimRunner {
    ResultManager start(InitParam initParam);
}
