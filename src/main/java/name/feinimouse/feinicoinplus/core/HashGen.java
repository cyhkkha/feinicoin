package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.*;

public interface HashGen {
    String hash(String content);
    Packer hash(BlockObj blockObj, String summary);
    Packer hash(Packer packer, String summary);
    AdmitPackerArr hash(Packer[] objArr, String[] summaryArr, Class<?> aClass);
}
