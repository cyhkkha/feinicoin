package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.BlockObj;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

public interface HashGenerator {
    String hash(String content);
    Packer hash(BlockObj blockObj);
    PackerArr hash(BlockObj[] objArr, Class<?> aClass);
    PackerArr hash(Packer[] objArr, Class<?> aClass);
}
