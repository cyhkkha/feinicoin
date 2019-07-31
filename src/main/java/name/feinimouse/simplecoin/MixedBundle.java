package name.feinimouse.simplecoin;

import lombok.Getter;
import name.feinimouse.feinicoin.account.Transaction;

public class MixedBundle {
    @Getter
    private Transaction transaction;
    @Getter
    private UTXOBundle utxo;
    @Getter
    private TransBundle transBundle;
    private boolean isAssets;
    
    public MixedBundle(Transaction t) {
        transaction = t;
        isAssets = false;
    }
    public MixedBundle(UTXOBundle utxo) {
        this.utxo = utxo;
        isAssets = true;
    }
    public MixedBundle(TransBundle tb) {
        transBundle = tb;
        isAssets = false;
    }
    public boolean isAssets() {
        return isAssets;
    }
}
