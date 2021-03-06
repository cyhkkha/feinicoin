package name.feinimouse.simplecoin.manager;

import name.feinimouse.simplecoin.account.Transaction;
import name.feinimouse.simplecoin.block.Block;
import name.feinimouse.simplecoin.block.Hashable;

import java.security.SignatureException;

public interface Verifier extends Hashable, Nameable {
    // 同步最新区块
    default void syncBlock(Block b) {
    }
    // 验证交易
    boolean verify(Transaction t) throws SignatureException;
    
}