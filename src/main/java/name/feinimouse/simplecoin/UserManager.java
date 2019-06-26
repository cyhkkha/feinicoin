package name.feinimouse.simplecoin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;

public class UserManager {
    private SM2Generator sm2Gen = SM2Generator.getInstance();;
    private Map<String, SM2> userMap;
    private String[] users;
    private Random random;

    private UserManager(String[] users) {
        var tempMap = new HashMap<String, SM2>();
        for (String user : users) {
            tempMap.put(user, sm2Gen.generateSM2());
        }
        this.userMap = tempMap;
        this.users = users;
        this.random = new Random();
    }
    public SM2 getSM2(String name) {
        return userMap.get(name);
    }
    public String getRandomUser() {
        return users[random.nextInt(users.length)];
    }
    public String getRandomUser(String not) {
        var res = users[random.nextInt(users.length)];
        if (!res.equals(not)) {
            return res;
        }
        return getRandomUser(not);
    }
    
}