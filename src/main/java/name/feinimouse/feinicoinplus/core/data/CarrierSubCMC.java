package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierSubCMC extends ClassMapContainer<Carrier> {
    public CarrierSubCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getSubClass();
    }

    public CarrierSubCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
