package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.mongodao.MongoDao;
import org.apache.commons.cli.*;

public class Cli {
    private static final String version = "0.7";
    
    private static final Integer[] TEST_COUNT = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    private static final int BUNDLE_SIZE = 20;
    private static final int UTXO_SIZE = 5;
    private static final double ASSET_RATE = 0.2;
    private static final int USER_COUNT = 100;
    private static final int CYCLE_TIME = 1;
    private static final String OUT_PATH = "E:\\\\Program Data\\experienal-data.txt";


    private Options options;
    private CommandLine cmd;
    private String[] args;
    
    public Cli(String[] args) {
        this.args = args;
        init();
    }
    
    public SimplecoinConfig parser() {
        var parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
            if ( args == null || args.length == 0 || cmd.hasOption("h")) {
                printHelp();
                return null;
            }
            if (cmd.hasOption("v")) {
                System.out.println("Version: " + version);
                return null;
            }
            return formatMode();
        } catch (Exception e) {
            System.out.println("---------------------");
            System.out.println(e.getMessage());
            System.out.println("---------------------");
            printHelp();
            return null;
        }
    }

    private void printHelp() {
        // 解析失败是用 HelpFormatter 打印 帮助信息
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            "java -jar simplecoin",
            options);
    }
    
    private SimplecoinConfig formatMode() throws ParseException {
        var config = new SimplecoinConfig();
        if (cmd.hasOption("net")) {
            MongoDao.isRemote(true);
            MongoDao.isAuth(true);
        }
        if (cmd.hasOption("d")) {
            MongoDao.init();
            MongoDao.drop();
            System.out.println("Database has been dropped");
            return null;
        }
        if (cmd.hasOption("b")) {
            config.baseInfo = true;
        }
        if (cmd.hasOption("a")) {
            config.account = true;
        } else if (cmd.hasOption("u")) {
            config.utxo = true;
        }
        if (cmd.hasOption("bcbdc")) {
            config.bcbdc = true;
        }
        if (!config.baseInfo && !config.account && !config.utxo && !config.bcbdc) {
            throw new ParseException("args should contains base_info or account or bcbdc");
        }
        config.USER_COUNT = getInt("user_count", USER_COUNT);
        config.BUNDLE_SIZE = getInt("bundle_size", BUNDLE_SIZE);
        config.UTXO_SIZE = getInt("utxo_size", UTXO_SIZE);
        config.ASSET_RATE = getDouble("asset_rate", ASSET_RATE);
        config.CYCLE_TIME = getInt("c", CYCLE_TIME);
        if (cmd.hasOption("test_count")) {
            var countStr = cmd.getOptionValue("test_count");
            var strArr = countStr.split(",");
            var intArr = new Integer[strArr.length];
            for (int i = 0; i < strArr.length; i ++) {
                try {
                    intArr[i] = Integer.parseInt(strArr[i].trim());
                } catch (NumberFormatException e) {
                    throw  new ParseException("test_count: expected integers split by ','"); 
                }
            }
            config.TEST_COUNT = intArr;
        } else {
            config.TEST_COUNT = TEST_COUNT;
        }
        
        config.output = cmd.getOptionValue("o", OUT_PATH);
        
        return config;
    }
    
    private void init() {
        options = new Options();
        
        // 查看基本信息
        options.addOption("b", "base_info", false, "Show machine base info");

        // 设置实验项目
        options.addOption("user_count", true, "Set the user count");
        options.addOption("bundle_size", true, "Set the bundle size");
        options.addOption("utxo_size", true, "Set the utxo size");
        options.addOption("asset_rate", true, "Set the asset rate");
        var testCount = Option.builder("test_count").hasArg().argName("count1,count2...")
            .desc("Set test count list with ',' separate to handle").build();
        options.addOption(testCount);
        
        // 选择模式
        options.addOption("a", "account", false, "Run account mode");
        options.addOption("u", "utxo", false, "Run utxo mode");
        options.addOption("bcbdc", false, "Run a mixed mode. \n Run bcbdc mode when use with '--account' or '--utxo'");
        
        // 查看版本
        options.addOption("v", "version", false, "Show version of simplecoin");
        // 输出位置
        options.addOption("o", "output", true, "Set the log output path");
        // 试验次数
        options.addOption("c", "cycle_time", true, "Set the cycle times of running");
        // 帮助
        options.addOption("h", "help", false, "get help");
        // 远程数据库
        options.addOption("net", false, "set the database remote");
        // 清除远程数据库
        options.addOption("d", "drop", false, "clear the remote database");
    }
    
    private int getInt(String name, int def) {
        if (cmd.hasOption(name)) {
            try {
                return Integer.parseInt(cmd.getOptionValue(name));
            } catch (NumberFormatException e) {
                System.out.println(name + ": expected a integer");
                System.exit(1);
                throw e;
            }
        }
        return def;
    }

    private double getDouble(String name, double def) {
        if (cmd.hasOption(name)) {
            try {
                return Double.parseDouble(cmd.getOptionValue(name));
            } catch (NumberFormatException e) {
                System.out.println(name + ": expected a double");
                System.exit(1);
                throw e;
            }
        }
        return def;
    }
}
