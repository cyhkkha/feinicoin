package name.feinimouse.feinicoinplus.core.node;

import name.feinimouse.feinicoinplus.core.base.SignObj;

public interface Verifier {
    <T> boolean commit(Callbacker<Boolean> callbacker, SignObj<T> signObj);
    <T> boolean commit(Callbacker<Boolean> callbacker, SignObj<T> signObj, String signer);
}
