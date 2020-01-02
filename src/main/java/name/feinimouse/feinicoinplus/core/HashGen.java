package name.feinimouse.feinicoinplus.core;

import name.feinimouse.feinicoinplus.core.data.AdmitPacker;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

public interface HashGen {
    String hash(String content);
    Packer hash(Packer obj, String summary);
    <T extends JsonAble> PackerArr<T> hash(AdmitPacker<T>[] objArr, String[] summaryArr);
}
