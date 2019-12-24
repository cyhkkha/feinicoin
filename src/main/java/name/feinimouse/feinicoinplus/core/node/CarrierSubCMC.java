package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierSubCMC extends ClassMapContainer<Carrier> {
    public CarrierSubCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getAttachSubClass();
    }
}
