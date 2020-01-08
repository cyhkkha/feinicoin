package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.CenterDao;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;

public class BaseCenContext extends CenterContext {

    public BaseCenContext(AccountManager accountManager, AssetManager assetManager, CenterDao centerDao) {
        super(accountManager, assetManager, centerDao);
    }
}
