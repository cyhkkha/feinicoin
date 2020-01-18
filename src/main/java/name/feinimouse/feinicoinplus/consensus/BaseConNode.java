package name.feinimouse.feinicoinplus.consensus;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
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
    protected ConNode net;
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
    protected boolean isConfirm = false;
    
    @Override
    public synchronized void consensus(ConMessage message) throws ConsensusException {
        if (nowTask != null) {
            // 若为同一状态则返回
            return;
        }

        Packer packer = message.getPacker();
        String center = packer.getCenter();
        PublicKey publicKey = publicKeyHub.getKey(center);
        if (signGenerator.verify(publicKey, packer, center)) {
            nowTask = new ConMessage(message, TYPE_CALLBACK);
            nowTask.setSender(address);
            signGenerator.sign(privateKey, nowTask, address);
            net.callback(nowTask);
        }
    }

    @Override
    public void callback(ConMessage message) throws ConsensusException {
        // 是否正在同步状态，若不在状态则进入状态
        if (nowTask == null) {
            consensus(message);
        }
        
        // 若已经完成同步则返回
        if (isConfirm) {
            return;
        }

        // 必须含有发送者和其签名
        String sender = message.getSender();
        PublicKey publicKey = publicKeyHub.getKey(sender);
        if (signGenerator.verify(publicKey, message, sender)) {
            nowTask.putSign(sender, message.getSign(sender));
            if (nowTask.signSize() >= nodeNum * 2 / 3) {
                nowTask.setType(TYPE_CONFIRM);
                net.confirm(nowTask);
                isConfirm = true;
            }
        }

    }

    @Override
    public synchronized void confirm(ConMessage message) throws ConsensusException {
        // 是否正在同步状态，若不在状态则进入状态
        if (nowTask == null) {
            consensus(message);
        }
        if (!isConfirm) {
            isConfirm = true;
        }
    }
}
