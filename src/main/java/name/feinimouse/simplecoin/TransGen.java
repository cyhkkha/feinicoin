package name.feinimouse.simplecoin;

import java.util.HashMap;
import java.util.Map;

import name.feinimouse.feinism2.SM2;
import name.feinimouse.feinism2.SM2Generator;

public class TransGen {
    private static SM2Generator sm2Gen = SM2Generator.getInstance();;
    private static Map<String, SM2> userMap;
    private static TransGen transGen;

    private TransGen() {}

    public TransGen init(String[] users) {
        var tempMap = new HashMap<String, SM2>();
        for (String user: users) {
            tempMap.put(user, sm2Gen.generateSM2());
        }
        TransGen.userMap = tempMap;
        TransGen.transGen = new TransGen();
        return getInstance();
    }

    public TransGen getInstance() {
        return transGen;
    }
}