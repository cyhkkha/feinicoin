package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

import java.util.Optional;

public class CarrierFetchCMC extends ClassMapContainer<Carrier> {
    public CarrierFetchCMC(Class<?> ...supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return Optional.ofNullable(carrier).map(Carrier::getFetchClass).orElse(null);
    }

    public CarrierFetchCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
