package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.*;

public interface HashGen {
    String hash(String content);
    Packer hash(Packer obj, String summary);
    AdmitPackerArr hash(AdmitPacker[] objArr, String[] summaryArr, Class<?> aClass);
    MerkelArr hash(BlockObj[] blockObjs, String[] summaryArr, Class<?> aClass);
    HashCover hash(BlockObj blockObj, String summary);
}
