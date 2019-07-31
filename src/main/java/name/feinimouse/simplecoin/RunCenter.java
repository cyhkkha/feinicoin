package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.manager.custome.*;
import name.feinimouse.utils.LoopUtils;

public class RunCenter extends Config {
    private static final int TRANS_SIZE = 1000;
    private final static int ASSET_SIZE = 200;
    private static final int UTXO_SIZE = 5;
    private final static int BUNDLE_SIZE = 20;
    
    public static void main(String[] args) {
        init();
        clear();
        
        final var sourceTrans = LoopUtils.loopToList(TRANS_SIZE, transGen::genSignedTransFa);
        final var utxoSource = LoopUtils.loopToList(TRANS_SIZE, () -> transGen.genUTXOBundle(UTXO_SIZE));
        final var assetsSource = LoopUtils.loopToList(TRANS_SIZE, () -> transGen.genMixedBundle(UTXO_SIZE));
        final var mixedSource = LoopUtils.loopToList(TRANS_SIZE - ASSET_SIZE, () -> transGen.genMixedBundle());
        LoopUtils.loop(ASSET_SIZE, () -> mixedSource.add(transGen.genMixedBundle(UTXO_SIZE)));

        var pureAccountCenter = new SimplePureAccountCenter(new SimplePureAccountOrder(userManager, sourceTrans));
        var bcbdcCenter = new SimpleBCBDCCenter(new SimpleBCBDCOrder(userManager, sourceTrans, BUNDLE_SIZE));
        var utxoCenter = new SimpleUTXOCenter(new SimpleUTXOOrder(userManager, utxoSource));
        var bcbdcUTXOCenter = new SimpleMixedBCBDCCenter(new SimpleMixedBCBDCOrder(userManager, assetsSource));
        bcbdcUTXOCenter.setName("纯UTXO并行模式");
        var mixedCenter = new SimpleMixedBCBDCCenter(new SimpleMixedBCBDCOrder(userManager, mixedSource, BUNDLE_SIZE));
        
        pureAccountCenter.activate();
        bcbdcCenter.activate();
        utxoCenter.activate();
        bcbdcUTXOCenter.activate();
        mixedCenter.activate();
        
        collectCenter(pureAccountCenter);
        collectCenter(bcbdcCenter);
        collectCenter(utxoCenter);
        collectCenter(bcbdcUTXOCenter);
        collectCenter(mixedCenter);
        
        System.exit(0);
    }
}
