package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.core.node.CenterContext;
import name.feinimouse.feinicoinplus.core.node.CenterDao;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("centerContext")
public class SimpleCenterContext extends CenterContext {

    @Autowired
    public SimpleCenterContext(AccountManager accountManager, AssetManager assetManager, CenterDao centerDao) {
        super(accountManager, assetManager, centerDao);
    }
}
