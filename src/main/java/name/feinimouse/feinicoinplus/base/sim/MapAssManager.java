package name.feinimouse.feinicoinplus.base.sim;

import name.feinimouse.feinicoinplus.core.crypt.HashGenerator;
import name.feinimouse.feinicoinplus.core.data.*;
import name.feinimouse.feinicoinplus.core.sim.AccountManager;
import name.feinimouse.feinicoinplus.core.sim.AddressManager;
import name.feinimouse.feinicoinplus.core.sim.AssetManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component("assetManager")
public class MapAssManager implements AssetManager, InitializingBean {

    private HashGenerator hashGenerator;
    private AddressManager addressManager;
    private AccountManager accountManager;

    private Map<String, Map<String, Asset>> assetAddressMap;
    private Map<String, Map<String, Queue<Packer>>> dynamicTrans;

    private Random random = new Random();

    private int size = 0;

    @Value("${NUMBER_ASSET}")
    private int NUMBER_ASSET;

    @Override
    public void afterPropertiesSet() throws Exception {
        genAsset(NUMBER_ASSET);
    }
    
    public MapAssManager() {
        assetAddressMap = new ConcurrentHashMap<>();
        dynamicTrans = new ConcurrentHashMap<>();
    }

    @Autowired
    public void setHashGenerator(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }

    @Autowired
    public void setAddressManager(AddressManager addressManager) {
        this.addressManager = addressManager;
    }

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public void genAsset(int number) {
        for (int i = 0; i < number; i++) {
            Account account = accountManager.getRandom();
            if (account == null) {
                break;
            }
            String owner = account.getAddress();
            String address = addressManager.getAddress();
            int coin = random.nextInt(100) + 1000;
            Asset asset = new Asset(address, owner, coin);
            put(asset);
        }
    }

    @Override
    public boolean contain(String address) {
        return assetAddressMap.containsKey(address);
    }

    @Override
    public boolean contain(String address, String owner) {
        if (contain(address)) {
            return assetAddressMap.get(address).containsKey(owner);
        }
        return false;
    }

    @Override
    public synchronized boolean put(Asset asset) {
        String address = asset.getAddress();
        String owner = asset.getOwner();
        // 非法账户不记录
        if (!accountManager.contain(owner)) {
            return false;
        }

        // 记录资产
        Map<String, Asset> addressMap = assetAddressMap.get(address);
        if (addressMap == null) {
            addressMap = new ConcurrentHashMap<>();
            assetAddressMap.put(address, addressMap);
            // 若账户已经存在则不记录
        } else if (addressMap.containsKey(owner)) {
            return false;
        }
        addressMap.put(owner, asset);

        // 记录资产变更记录
        Map<String, Queue<Packer>> transMap = dynamicTrans.get(address);
        if (transMap == null) {
            transMap = new ConcurrentHashMap<>();
            dynamicTrans.put(address, transMap);
        }

        // 若资产存在变更记录则引用以前的记录
        Queue<Packer> queue = new ConcurrentLinkedQueue<>();
        if (asset.getHistories() != null) {
            Packer[] histories = (Packer[]) asset.getHistories().obj();
            Collections.addAll(queue, histories);
        } else {
            Packer packer = hashGenerator.hash(AssetTrans.init(asset));
            queue.add(packer);
        }
        transMap.put(owner, queue);

        size++;
        return true;
    }

    private <T> T getRandomFromMap(Map<?, T> map) {
        if (map.size() <= 0) {
            return null;
        }
        int index = random.nextInt(map.size());
        Object key = map.keySet().toArray()[index];
        return map.get(key);
    }

    public Asset getRandom() {
        Map<String, Asset> userMap = getRandomFromMap(assetAddressMap);
        if (userMap != null) {
            return getRandomFromMap(userMap);
        }
        return null;
    }

    @Override
    public Packer[] getHistories(String address, String owner) {
        return Optional.ofNullable(dynamicTrans.get(address))
            .map(map -> map.get(owner))
            .map(q -> q.toArray(Packer[]::new))
            .orElse(null);
    }

    @Override
    public Asset get(String address, String owner) {
        return Optional.ofNullable(assetAddressMap.get(address))
            .map(map -> map.get(owner))
            .orElse(null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public synchronized boolean remove(String address, String owner) {
        if (contain(address)) {
            Map<String, Asset> map = assetAddressMap.get(address);
            if (map.containsKey(owner)) {
                map.remove(owner);
                if (map.size() <= 0) {
                    assetAddressMap.remove(address);
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public synchronized PackerArr pack() {
        Queue<Asset> assets = new ConcurrentLinkedQueue<>();
        for (String address : assetAddressMap.keySet()) {
            Map<String, Asset> map = assetAddressMap.get(address);
            for (String user : map.keySet()) {
                Asset asset = map.get(user);
                Queue<Packer> queue = dynamicTrans.get(address).get(user);
                Packer[] packers = queue.toArray(Packer[]::new);
                PackerArr packerArr = hashGenerator.hash(packers, AssetTrans.class);
                asset.setHistories(packerArr);
                assets.add(asset);
            }
        }
        return hashGenerator.hash(assets.toArray(Asset[]::new), Asset.class);
    }

    @Override
    public synchronized boolean commit(Packer packer) {
        if (!packer.objClass().equals(AssetTrans.class)) {
            return false;
        }

        AssetTrans assetTrans = (AssetTrans) packer.obj();
        String senderAdd = assetTrans.getOperator();
        String receiverAdd = assetTrans.getReceiver();
        String assetAdd = assetTrans.getAddress();

        if (contain(assetAdd, senderAdd)) {
            Asset sender = get(assetAdd, senderAdd);
            Asset receiver = get(assetAdd, receiverAdd);
            // 若接收方没有对应资产则创建
            if (receiver == null) {
                receiver = sender.copy();
                receiver.setOwner(receiverAdd);
                receiver.setNumber(0);
                put(receiver);
            }

            // 改变资产的数额
            int coin = assetTrans.getNumber();
            receiver.setNumber(receiver.getNumber() + coin);
            sender.setNumber(sender.getNumber() - coin);

            // 引用变更记录
            Queue<Packer> senderQ = dynamicTrans.get(assetAdd).get(senderAdd);
            Queue<Packer> receiverQ = dynamicTrans.get(assetAdd).get(receiverAdd);
            senderQ.add(packer);
            receiverQ.add(packer);

            // 若账户也发生变更，则做对应的更改
            if (assetTrans.getTransaction() != null) {
                accountManager.commit(assetTrans.getTransaction());
            }

            return true;
        }
        return false;
    }
    
}
