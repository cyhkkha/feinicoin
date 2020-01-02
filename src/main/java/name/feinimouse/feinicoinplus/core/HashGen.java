package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.AdmitPacker;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.AdmitPackerArr;

public interface HashGen {
    String hash(String content);
    Packer hash(Packer obj, String summary);
    AdmitPackerArr hash(AdmitPacker[] objArr, String[] summaryArr, Class<?> aClass);
}
