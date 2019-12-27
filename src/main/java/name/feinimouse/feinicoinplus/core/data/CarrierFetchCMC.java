package name.feinimouse.feinicoinplus.core.data;

import name.feinimouse.utils.ClassMapContainer;

public class CarrierFetchCMC extends ClassMapContainer<Carrier> {
    public CarrierFetchCMC(Class<?>[] supportClass) {
        super(supportClass);
    }

    @Override
    public Class<?> getCoverClass(Carrier carrier) {
        return carrier.getFetchClass();
    }

    public CarrierFetchCMC(Class<?>[] supportClass, int max) {
        super(supportClass, max);
    }
}
