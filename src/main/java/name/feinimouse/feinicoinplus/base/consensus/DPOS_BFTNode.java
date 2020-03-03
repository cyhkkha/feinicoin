package name.feinimouse.feinicoinplus.base.consensus;

import name.feinimouse.feinicoinplus.consensus.AbstractBFTNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PublicKey;
import java.util.concurrent.ConcurrentHashMap;

public class DPOS_BFTNode extends AbstractBFTNode {
    private Logger logger = LogManager.getLogger(DPOS_BFTNode.class);

    @Override
    public void start(BFTMessage bftMessage) {
        // 生成prePrepareMessage
        prepareMessage = bftMessage.clone();
        prepareMessage.setSign(signGenerator.sign(privateKey, prepareMessage.getMessage()));
        prepareMessage.setSigner(address);
        prepareMessage.setAttachSignMap(new ConcurrentHashMap<>());

        stage = STAGE_PREPARE;
        logger.trace("节点@{} 收到原始消息，将广播全网", address);
        net.prepare(prepareMessage.clone());
    }

    @Override
    public void prePrepare(BFTMessage bftMessage) {
        if (stage.equals(STAGE_PREPARE)) {
            String sign = bftMessage.getSign();
            String signer = bftMessage.getSigner();
            PublicKey publicKey = publicKeyHub.getKey(signer);
            if (signGenerator.verify(publicKey, sign, bftMessage.getMessage())) {
                var signMap = prepareMessage.getAttachSignMap();
                signMap.put(signer, sign);
                if (signMap.size() >= nodeNum * 2 / 3 + 1) {
                    turn2Commit();
                }
            }
        }
    }


    @Override
    public void prepare(BFTMessage bftMessage) {
        String sign = bftMessage.getSign();
        String signer = bftMessage.getSigner();
        PublicKey publicKey = publicKeyHub.getKey(signer);
        if (signGenerator.verify(publicKey, sign, bftMessage.getMessage())) {
            BFTMessage message = bftMessage.clone();
            message.setSign(signGenerator.sign(privateKey, message.getMessage()));
            message.setSigner(address);
            logger.trace("节点@{} 收到 prepare 消息，将返回签名", address);
            net.prePrepare(message);
        }
    }

    @Override
    public void commit(BFTMessage bftMessage) {
        var signMap = bftMessage.getAttachSignMap();
        if (signMap.size() >= nodeNum * 2 / 3 + 1) {
            boolean pass = signMap.keySet().stream().allMatch(signer -> {
                PublicKey publicKey = publicKeyHub.getKey(signer);
                String sign = signMap.get(signer);
                return signGenerator.verify(publicKey, sign, bftMessage.getMessage());
            });
            if (pass) {
                turn2Reply();
            }
        }
    }

    @Override
    public void reply() {
        // 返回停止状态等待下一轮共识
        stage = STAGE_STOP;
        prepareMessage = null;
    }

    private synchronized void turn2Commit() {
        if (stage.equals(STAGE_PREPARE)) {
            stage = STAGE_COMMIT;
            logger.trace("节点@{} 收集到 2/3 的确认信息，将广播全网", address);
            net.commit(prepareMessage.clone());
            turn2Reply();
        }
    }
    
    private synchronized void turn2Reply() {
        stage = STAGE_REPLY;
        logger.trace("节点@{} 达成共识", address);
        net.reply();
    }
}
