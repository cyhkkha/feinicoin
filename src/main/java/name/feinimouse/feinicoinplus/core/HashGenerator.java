package name.feinimouse.feinicoinplus.core;

public interface HashGenerator {
    String hash(String content);
    Packer hash(BlockObj blockObj);
    PackerArr hash(BlockObj[] objArr, Class<?> aClass);
    PackerArr hash(Packer[] objArr, Class<?> aClass);
}
