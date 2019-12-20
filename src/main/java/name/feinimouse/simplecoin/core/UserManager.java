package name.feinimouse.simplecoin.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Getter;
import name.feinimouse.simplecoin.feinism2.SM2;
import name.feinimouse.simplecoin.feinism2.SM2Generator;
import name.feinimouse.utils.LoopUtils;
import net.openhft.hashing.LongHashFunction;

public class UserManager {
    private Map<String, SM2> userMap;
    private Map<String, List<String>> addressMap;
    @Getter
    private String[] users;
    private Random random;
    @Getter
    private int addressLimit;

    /**
     * 初始化用户管理器
     * @param users 用户列表
     * @param addressLimit 一个用户拥有的地址限制
     */
    public UserManager(String[] users, int addressLimit) {
        // 初始化密钥生成器
        var sm2Gen = SM2Generator.getInstance();
        // 初始化密钥对应map和随机地址对应map
        this.userMap = new HashMap<>();
        this.addressMap = new HashMap<>();
        var xx = LongHashFunction.xx();
        for (String user : users) {
            userMap.put(user, sm2Gen.generateSM2());
            var addressList = LoopUtils.loopToList(addressLimit, () ->
                String.valueOf(xx.hashLong(System.currentTimeMillis())));
            addressMap.put(user, addressList);
        }
        // 其他初始化
        this.addressLimit = addressLimit;
        this.users = users;
        this.random = new Random();
    }
    
    public UserManager(String[] users) {
        this(users, 10);
    }
    public SM2 getSM2(String name) {
        return userMap.get(name);
    }

    /**
     * 获取一个随机用户
     * @return 随机用户
     */
    public String getRandomUser() {
        return users[random.nextInt(users.length)];
    }

    /**
     * 获取一个随机用户，该用户和输入参数的用户不同
     * @param not 要过滤的用户
     * @return 随机用户
     */
    public String getRandomUser(String not) {
        var res = users[random.nextInt(users.length)];
        if (!res.equals(not)) {
            return res;
        }
        return getRandomUser(not);
    }

    /**
     * 获取一个用户的随机地址
     * @param user 要获取的用户
     * @return 随机地址
     */
    public String getRandomAddress(String user) {
        return addressMap.get(user).get(random.nextInt(addressLimit));
    }
    
}