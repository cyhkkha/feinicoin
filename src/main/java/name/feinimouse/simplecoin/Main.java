package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.core.SimplecoinRunner;
import name.feinimouse.simplecoin.core.impl.*;
import name.feinimouse.simplecoin.mongodao.MongoDao;
import name.feinimouse.utils.LoopUtils;

import java.io.IOException;

public class Main {
    private static String OUT_PATH;
    private static int CYCLE_TIME;

    public static void main(String[] args) {
//        System.out.println(Arrays.toString(args));

        var cli = new Cli(args);
        SimplecoinConfig config = cli.parser();
        if (config == null) {
            System.exit(0);
            return;
        }
        
        System.out.println(config);
        // 输出位置
        OUT_PATH = config.output;
        // 循环次数
        CYCLE_TIME = config.CYCLE_TIME;

        MongoDao.init();
        
        // 查看基本信息
        if (config.baseInfo) {
            var show = new BaseInfoRunner();
            show.showInfo();
            System.exit(0);
            return;
        }
        
        if (config.account) {
            if (config.bcbdc) {
                runSimpleCoin(new BCBDCAccountRunner(config), "BCBDC账户模式");
            }
            runSimpleCoin(new PureAccountRunner(config), "纯账户模式");
        } else if (config.utxo) {
            if (config.bcbdc) {
                runSimpleCoin(new BCBDCUTXORunner(config), "BCBDC UTXO 模式");
            }
            runSimpleCoin(new UTXORunner(config), "纯UTXO模式");
        } else if (config.bcbdc) {
            runSimpleCoin(new MixedRunner(config), "BCBDC 混合模式");
        }

        System.exit(0);
    }
    
    private static void runSimpleCoin(SimplecoinRunner runner, String name)  {
        LoopUtils.loop(CYCLE_TIME, () -> {
            try {
                var stat = runner.run();
                stat.output(name, OUT_PATH);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("找不到文件！！！！");
                System.exit(1);
            }
        });
    }
    
}
