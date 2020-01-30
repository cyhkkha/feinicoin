package name.feinimouse.feinicoinplus.base.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.consensus.ConMessage;
import name.feinimouse.feinicoinplus.consensus.ConNode;
import name.feinimouse.feinicoinplus.core.crypt.PublicKeyHub;
import name.feinimouse.feinicoinplus.core.crypt.SignGenerator;
import name.feinimouse.feinicoinplus.core.data.Packer;

import java.security.PrivateKey;
import java.security.PublicKey;

public class BaseConNode implements ConNode {
    @Setter
    @Getter
    protected String address;

    @Setter
    protected ConNodeNet net;
    @Setter
    protected SignGenerator signGenerator;
    @Setter
    protected PublicKeyHub publicKeyHub;
    
    @Setter
    protected PrivateKey privateKey;
    @Setter
    protected int nodeNum;

    protected ConMessage nowTask;
    protected int taskId = 0;
    
    @Override
    public synchronized void consensus(ConMessage message) {
        if (nowTask != null || message.getId() <= taskId) {
            // 若为同一状态则返回
            return;
        }

        Packer packer = message.getPacker();
        String center = packer.getCenter();
        PublicKey publicKey = publicKeyHub.getKey(center);
        if (signGenerator.verify(publicKey, packer, center)) {
            nowTask = new ConMessage(message, TYPE_CALLBACK);
            taskId = nowTask.getId();
            nowTask.setSender(address);
            signGenerator.sign(privateKey, nowTask, address);
            net.callback(nowTask);
        }
    }

    @Override
    public void callback(ConMessage message) {
        // 是否正在同步状态，若不在状态则进入状态
        if (nowTask == null && message.getId() > taskId) {
            consensus(message);
        }
        // 是否和当前是同一个任务
        if (!nowTask.equals(message)) {
            return;
        }

        // 必须含有发送者和其签名
        String sender = message.getSender();
        PublicKey publicKey = publicKeyHub.getKey(sender);
        if (signGenerator.verify(publicKey, message, sender)) {
            nowTask.putSign(sender, message.getSign(sender));
            // 如果积累的签名数量大于2/3则完成同步并通知全网
            if (nowTask.signSize() >= nodeNum * 2 / 3) {
                nowTask.setType(TYPE_CONFIRM);
                endRound(nowTask);
                net.confirm(nowTask);
            }
        }

    }

    @Override
    public synchronized void confirm(ConMessage message) {
        // 是否正在同步状态，若不在状态则进入状态
        if (nowTask == null && message.getId() > taskId) {
            consensus(message);
        }
        // 是否和当前是同一个任务
        if (!nowTask.equals(message)) {
            return;
        }
        
        // 签名数量要大于2/3
        if (message.signSize() >= nodeNum * 2 / 3) {
            // 验证所有的签名
            boolean isConfirm = message.getSignMap().keySet()
                .stream().allMatch(address -> signGenerator
                    .verify(publicKeyHub.getKey(address), message, address));
            if (isConfirm) {
                endRound(message);
            }
        }
    }

    @Override
    public void endRound(ConMessage message) {
        nowTask = null;
    }

    @Override
    public boolean isConfirm() {
        return nowTask == null;
    }
}
