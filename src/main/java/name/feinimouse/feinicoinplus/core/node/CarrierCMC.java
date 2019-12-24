package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierCMC extends ClassMapContainer<Carrier> {
    public CarrierCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getAttachClass();
    }
    
}
