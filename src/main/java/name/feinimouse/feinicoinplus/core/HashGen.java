package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.*;

public interface HashGen {
    String hash(String content);
    Packer hash(BlockObj blockObj);
    PackerArr hash(BlockObj[] objArr, Class<?> aClass);
    PackerArr hash(Packer[] objArr, Class<?> aClass);
}
