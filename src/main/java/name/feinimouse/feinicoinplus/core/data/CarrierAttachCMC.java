package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

import java.util.Optional;

public class CarrierAttachCMC extends ClassMapContainer<Carrier> {
    public CarrierAttachCMC(Class<?> ...supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return Optional.ofNullable(carrier).map(Carrier::getPacker).map(Packer::objClass).orElse(null);
    }

    public CarrierAttachCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
