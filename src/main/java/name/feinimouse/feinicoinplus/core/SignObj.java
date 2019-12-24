package name.feinimouse.feinicoinplus.core;

public interface SignObj<T> extends HashObj<T> {
    String getSign(String signer);
    String deleteSign(String signer);
    SignObj<T> putSign(String signer, String sign);
    int signSize();
}
