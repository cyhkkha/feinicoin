package name.feinimouse.simplecoin.manager;

import java.util.List;

import name.feinimouse.feinicoin.manager.Center;

public class CenterManager {
    protected List<Center> centerList;
    
    public void addCenter(Center center) {
        centerList.add(center);
    }
}