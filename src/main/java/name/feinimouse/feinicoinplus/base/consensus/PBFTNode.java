package name.feinimouse.feinicoinplus.base.consensus;

import name.feinimouse.feinicoinplus.consensus.AbstractBFTNode;
import name.feinimouse.feinicoinplus.consensus.BFTMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PublicKey;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PBFTNode extends AbstractBFTNode {
    private Logger logger = LogManager.getLogger(PBFTNode.class);

    private ConcurrentLinkedQueue<String> preparedNode = new ConcurrentLinkedQueue<>();
    
    
    @Override
    public synchronized void start(BFTMessage bftMessage) {
        // 生成prePrepareMessage
        prepareMessage = bftMessage.clone();
        prepareMessage.setSign(signGenerator.sign(privateKey, prepareMessage.getMessage()));
        prepareMessage.setSigner(address);
        prepareMessage.setAttachSignMap(new ConcurrentHashMap<>());

        // 切换状态 STAGE_PREPARE 并广播验证结果
        stage = STAGE_PREPARE;
        
        logger.trace("节点@{} 收到原始消息进入 STAGE_PREPARE 状态", address);
        net.prepare(prepareMessage.clone());
    }

    @Override
    public void prePrepare(BFTMessage bftMessage) {
        // 无该状态
    }

    @Override
    public void prepare(BFTMessage bftMessage) {
        // 如果是 STAGE_STOP 状态停止的节点则自然转换到 STAGE_PREPARE 状态
        if (stage.equals(STAGE_STOP)) {
            turn2Prepare(bftMessage);
            return;
        }

        // 如果是停止的节点已经是 STAGE_PREPARE 状态则进行对其他节点 STAGE_PREPARE 状态收集
        if (stage.equals(STAGE_PREPARE) && bftMessage.getMessage().equals(prepareMessage.getMessage())) {
            // 先对消息进行验证
            BFTMessage message = bftMessage.clone();
            PublicKey publicKey = publicKeyHub.getKey(message.getSigner());
            String sign = message.getSign();
            String signer = message.getSigner();
            if (signGenerator.verify(publicKey, sign, message.getMessage())) {
                // 将消息加入收集
                prepareMessage.getAttachSignMap().put(signer, sign);
                // 若收集到 2/3 + 1 个 STAGE_PREPARE状态的签名则自然转换到 STAGE_COMMIT 状态
                if (prepareMessage.getAttachSignMap().size() >= nodeNum * 2 / 3 + 1) {
                    turn2Commit();
                }
            }
        }
    }

    @Override
    public void commit(BFTMessage bftMessage) {
        // 若非 STAGE_PREPARE 或 STAGE_COMMIT 状态的节点接到消息将直接返回
        if (!stage.equals(STAGE_PREPARE)
            && !stage.equals(STAGE_COMMIT)
            | !bftMessage.getMessage().equals(prepareMessage.getMessage())) {
            return;
        }
        BFTMessage message = bftMessage.clone();
        String signer = message.getSigner();
        ConcurrentHashMap<String, String> receivedMap = message.getAttachSignMap();
        ConcurrentHashMap<String, String> myMap = prepareMessage.getAttachSignMap();

        // 首先将消息中的签名合并到本地
        receivedMap.forEach((key, value) -> {
            if (!myMap.containsKey(key)) {
                PublicKey publicKey = publicKeyHub.getKey(key);
                if (signGenerator.verify(publicKey, value, message.getMessage())) {
                    myMap.put(key, value);
                }
            }
        });
        
        // 如果是 STAGE_PREPARE 状态
        // 且自身满足收集到 2/3 + 1 个 STAGE_PREPARE 状态
        // 节点则自然进行跨越至 STAGE_PREPARE 状态
        if (stage.equals(STAGE_PREPARE) && myMap.size() >= nodeNum * 2 / 3 + 1) {
            turn2Commit();
        }

        // 如果节点是 STAGE_COMMIT 状态
        // 且commit的节点也收到了2/3 + 1 个 STAGE_PRE_PREPARE 状态
        // 节点则记录下满足commit的节点
        if (stage.equals(STAGE_COMMIT)
            && receivedMap.size() >= nodeNum * 2 / 3 + 1
            && !preparedNode.contains(signer)) {
            preparedNode.add(signer);
            // 节点若发现收到2/3 + 1 个 commit 的节点则完成共识
            if (preparedNode.size() >= nodeNum * 2 / 3 + 1) {
                turn2Reply();
            }
        }

    }

    @Override
    public void reply() {
        // 返回停止状态等待下一轮共识
        stage = STAGE_STOP;
        prepareMessage = null;
        preparedNode.clear();
    }

    private synchronized void turn2Prepare(BFTMessage bftMessage) {
        if (stage.equals(STAGE_STOP)) {
            BFTMessage message = bftMessage.clone();
            PublicKey publicKey = publicKeyHub.getKey(message.getSigner());
            String sign = message.getSign();
            String signer = message.getSigner();
            
            // 对prepare消息进行检验
            if (signGenerator.verify(publicKey, sign, message.getMessage())) {
                message.setSigner(address);
                message.setSign(signGenerator.sign(privateKey, message.getMessage()));

                // 生成 prePrepareMessage
                prepareMessage = message.clone();
                prepareMessage.setAttachSignMap(new ConcurrentHashMap<>());
                prepareMessage.getAttachSignMap().put(signer, sign);

                // 进入 STAGE_PREPARE 状态并广播
                stage = STAGE_PREPARE;
                logger.trace("节点@{} 收到 prepare 消息进入 STAGE_PREPARE 状态", address);
                net.prepare(message);
            }
        }
    }

    private synchronized void turn2Commit() {
        if (stage.equals(STAGE_PREPARE)) {
            // 将自己的签名也放入 prePrepareMessage
            String sign = prepareMessage.getSign();
            String signer = prepareMessage.getSigner();
            prepareMessage.getAttachSignMap().put(signer, sign);
            
            // 进入commit状态并广播
            stage = STAGE_COMMIT;
            logger.trace("节点@{} 收集到 2/3 的 prepare 消息进入 STAGE_COMMIT 状态", address);
            net.commit(prepareMessage.clone());
        }
    }
    

    private synchronized void turn2Reply() {
        if (stage.equals(STAGE_COMMIT)) {
            // 进入 STAGE_REPLY 状态并广播
            stage = STAGE_REPLY;
            logger.trace("节点@{} 收集到 2/3 的 commit 消息，达成共识", address);
            net.reply();
        }
    }

}
