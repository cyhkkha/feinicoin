package name.feinimouse.feinicoinplus.base.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.consensus.ConMessage;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import name.feinimouse.feinicoinplus.consensus.ConfirmTag;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseConNode implements ConNode {
    private Logger logger = LogManager.getLogger(BaseConNode.class);

    @Setter
    @Getter
    protected String address;

    @Setter
    protected ConNodeNet net;
    @Setter
    protected SignGenerator signGenerator;
    @Setter
    protected HashGenerator hashGenerator;
    @Setter
    protected PublicKeyHub publicKeyHub;

    @Setter
    protected PrivateKey privateKey;
    @Setter
    protected int nodeNum;

    protected ConMessage nowTask;
    protected int taskId = 0;
    protected long consensusStartTime;

    @Override
    public synchronized void consensus(ConMessage message) {
        if (!isConfirm() || message.getId() <= taskId) {
            // 若为同一状态则返回
            return;
        }

        String sender = message.getSender();
        PublicKey publicKey = publicKeyHub.getKey(sender);
        boolean isConsensus = signGenerator.verify(publicKey, message, sender);
//        logger.trace("-------");
        if (isConsensus) {
            // 记录当前的任务状况
            nowTask = message.clone(TYPE_CONFIRM, address);
            signGenerator.sign(privateKey, nowTask, address);
            taskId = nowTask.getId();

            // 将回应信息转发
            ConMessage nextMessage = nowTask.clone(TYPE_CALLBACK);
            logger.trace("节点 {} 共识（id: {}）开始", address, message.getId());
            consensusStartTime = System.currentTimeMillis();
            net.callback(nextMessage);
        }
    }

    private void genConfirmTag() {
        String[] signers = nowTask.getSignMap().keySet().toArray(String[]::new);
        ConfirmTag confirmTag = new ConfirmTag(signers);
        String summary = Arrays.stream(signers).map(nowTask::getSign)
            .collect(Collectors.joining(","));
        String hash = hashGenerator.hash(summary);
        String sign = signGenerator.sign(privateKey, hash);
        confirmTag.setHash(hash);
        confirmTag.setSign(sign);
        nowTask.setConfirmTag(confirmTag);
    }

    @Override
    public synchronized void callback(ConMessage message) {
        // 是否正在同步状态，若不在状态则进入状态
        if (isConfirm() && message.getId() > taskId) {
            consensus(message);
        }
        // 是否和当前是同一个任务
        if (message.notSameTask(nowTask)) {
            return;
        }

        // 必须含有发送者和其签名
        String sender = message.getSender();
        PublicKey publicKey = publicKeyHub.getKey(sender);
        if (signGenerator.verify(publicKey, message, sender)) {
            nowTask.putSign(sender, message.getSign(sender));
            // 如果积累的签名数量大于2/3则完成同步并通知全网
            if (nowTask.signSize() >= nodeNum * 2 / 3 && !isConfirm()) {
                genConfirmTag();

                logger.trace("节点 {} 发送完全确认消息，耗时 {}ms"
                    , address, System.currentTimeMillis() - consensusStartTime);
                net.confirm(nowTask);

                endRound(nowTask);
            }
        }

    }

    @Override
    public synchronized void confirm(ConMessage message) {
        // 是否正在同步状态，若不在状态则进入状态
        if (isConfirm() && message.getId() > taskId) {
            consensus(message);
        }
        // 是否和当前是同一个任务
        if (message.notSameTask(nowTask) || message.getConfirmTag() == null) {
            return;
        }

        logger.trace("节点 {} 收到完全确认消息（来自: {}），耗时 {}ms"
            , address, message.getSender()
            , System.currentTimeMillis() - consensusStartTime);
        // 签名数量要大于2/3
        if (message.signSize() >= nodeNum * 2 / 3) {
            // 验证合法性
            ConfirmTag confirmTag = message.getConfirmTag();
            String[] signers = confirmTag.getSignerArr();
            PublicKey publicKey = publicKeyHub.getKey(message.getSender());

            boolean isConfirm =
                // 所有签名必须存在
                Arrays.stream(signers).allMatch(message::hasSign) &&
                    // 总签名必须验证通过
                    signGenerator.verify(publicKey, confirmTag.getSign(), confirmTag.getHash());

            if (isConfirm) {
                logger.trace("节点 {} 完全确认消息通过"
                    , address);
                endRound(message);
            }
        }
    }

    @Override
    public void endRound(ConMessage message) {
        logger.trace("节点 {} 完成共识, 耗时 {}ms", address,
            System.currentTimeMillis() - consensusStartTime);
        nowTask = null;
    }

    @Override
    public boolean isConfirm() {
        return nowTask == null;
    }
}
