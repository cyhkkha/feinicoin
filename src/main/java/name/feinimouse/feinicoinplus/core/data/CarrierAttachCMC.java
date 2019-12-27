package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierAttachCMC extends ClassMapContainer<Carrier> {
    public CarrierAttachCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getAttach().objClass();
    }

    public CarrierAttachCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
