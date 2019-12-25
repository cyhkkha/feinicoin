package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierCMC extends ClassMapContainer<Carrier> {
    public CarrierCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getAttachClass();
    }

    public CarrierCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
