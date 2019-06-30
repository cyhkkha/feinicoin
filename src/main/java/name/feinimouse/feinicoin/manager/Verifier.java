package name.feinimouse.feinicoin.manager;

import name.feinimouse.feinicoin.account.Transaction;
import name.feinimouse.feinicoin.block.Block;
import name.feinimouse.feinicoin.block.Hashable;

import java.security.SignatureException;

public interface Verifier extends Hashable, Nameable {
    // 同步最新区块
    default void syncBlock(Block b) {
    }
    // 验证交易
    boolean verify(Transaction t) throws SignatureException;
    
}