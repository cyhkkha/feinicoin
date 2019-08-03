package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.core.*;

public class Main {
    private static Integer[] TEST_COUNT = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
    private static int BUNDLE_SIZE = 20;
    private static int UTXO_SIZE = 5;
    private static double ASSET_RATE = 0.2;
    private static int USER_COUNT = 100;
    
    private static Config config;

    public static void main(String[] args) {
        config = new Config(USER_COUNT, TEST_COUNT, BUNDLE_SIZE, UTXO_SIZE, ASSET_RATE);
    }
    
    
    public static void runBCBDCAccount() {
        var runner = new RunBCBDCAccount(config);
        var stat = runner.run();
        stat.print("BCBDC账户模式");
    }
    
    public static void runBCBDCUTXO() {
        var runner = new RunBCBDCUTXO(config);
        var stat = runner.run();
        stat.print("BCBDC UTXO模式");
    }
    
    public static void runMixed() {
        var runner = new RunMixed(config);
        var stat = runner.run();
        stat.print("Mixed模式");
    }
    public static void runPureAccount() {
        var runner = new RunPureAccount(config);
        var stat = runner.run();
        stat.print("纯账户模式");
    }
    public static void runUTXO() {
        var runner = new RunUTXO(config);
        var stat = runner.run();
        stat.print("纯UTXO模式");
    }
}
