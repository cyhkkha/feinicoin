package name.feinimouse.feinicoinplus.deprecated.consensus;

import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OptimizedConNode extends ClassicalConNode implements ConNode {
    private Logger logger = LogManager.getLogger(OptimizedConNode.class);

    @Setter
    protected HashGenerator hashGenerator;

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
    protected void handelCallback() {
        // 如果积累的签名数量大于2/3则完成同步并通知全网
        if (nowTask.signSize() >= nodeNum * 2 / 3 && !isConfirm()) {
            genConfirmTag();

            logger.trace("节点 {} 发送完全确认消息", address);
            net.confirm(nowTask);

            endRound(nowTask);
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

}
