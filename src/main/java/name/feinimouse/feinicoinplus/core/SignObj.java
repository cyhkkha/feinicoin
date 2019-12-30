package name.feinimouse.feinicoinplus.core;

public interface SignObj extends HashObj {
    String getSign(String signer);

    String deleteSign(String signer);

    SignObj putSign(String signer, String sign);

    boolean containSign(String signer);

    int signSize();
}
