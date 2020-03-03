package name.feinimouse.feinicoinplus.base;

import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNet;
import name.feinimouse.feinicoinplus.base.consensus.DPOS_BFTNode;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNet;
import name.feinimouse.feinicoinplus.base.consensus.PBFTNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import name.feinimouse.utils.LoopUtils;
import org.junit.Test;

public class TestConsensus extends BaseTest {
    
    @Test
    public void testPBFT() throws InterruptedException {
        for (int n = 0; n < 5; n ++) {
            PBFTNet pbftNet = (PBFTNet) context.getBean("pbftNet");
            LoopUtils.loop(34, () -> {
                PBFTNode pbftNode = (PBFTNode) context.getBean("pbftNode");
                pbftNet.putNode(pbftNode);
            });
            BFTMessage bftMessage = new BFTMessage();
            bftMessage.setMessage("test ss ss ss");
            long startTime = System.currentTimeMillis();
            pbftNet.start(bftMessage);
            while (!pbftNet.isConsensus()) {
                Thread.sleep(10);
                if (System.currentTimeMillis() - startTime > 30 * 1000) {
                    break;
                }
            }
//            logger.info("共识时间：{} ms", System.currentTimeMillis() - startTime);
            System.out.println(System.currentTimeMillis() - startTime);
            pbftNet.destroy();
        }
    }

    @Test
    public void testDPOS_BFT() throws InterruptedException {
        for (int n = 0; n < 5; n ++) {
            DPOS_BFTNet dpos_bftNet = (DPOS_BFTNet) context.getBean("dpos_bftNet");
            LoopUtils.loop(32, () -> {
                DPOS_BFTNode dpos_bftNode = (DPOS_BFTNode) context.getBean("dpos_bftNode");
                dpos_bftNet.putNode(dpos_bftNode);
            });
            BFTMessage bftMessage = new BFTMessage();
            bftMessage.setMessage("test ss ss ss");
            long startTime = System.currentTimeMillis();
            dpos_bftNet.start(bftMessage);
            while (!dpos_bftNet.isConsensus()) {
                Thread.sleep(10);
                if (System.currentTimeMillis() - startTime > 10 * 1000) {
                    break;
                }
            }
//            logger.info("共识时间：{} ms", System.currentTimeMillis() - startTime);
            System.out.println(System.currentTimeMillis() - startTime);
            dpos_bftNet.destroy();
        }
    }
    
}
