package name.feinimouse.simplecoin;

import name.feinimouse.simplecoin.core.*;
import name.feinimouse.simplecoin.core.impl.*;
import name.feinimouse.utils.LoopUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    private static Integer[] TEST_COUNT = { 100, 500, 1000, 1500, 2000, 2500, 3000, 4000, 5000, 6000, 8000, 10000 };
//    private static Integer[] TEST_COUNT = { 100, 200 };
    private static int BUNDLE_SIZE = 20;
//    private static int BUNDLE_SIZE = 10;
    private static int UTXO_SIZE = 5;
    private static double ASSET_RATE = 0.2;
    private static int USER_COUNT = 100;
    
    private static String OUT_PATH = "E:\\\\Program Data\\experienal-data.txt";
    
    private static Config config;

    public static void main(String[] args) {
        config = new Config(USER_COUNT, TEST_COUNT, BUNDLE_SIZE, UTXO_SIZE, ASSET_RATE);
//        LoopUtils.loopBreak(5, () -> {
//            try {
//                runBCBDCUTXO();
//                return true;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//        });
        System.out.println(config.toString());
        System.out.println(Arrays.toString(args));
        System.exit(0);
    }
    
    
    public static void runBCBDCAccount() throws FileNotFoundException {
        var runner = new RunBCBDCAccount(config);
        var stat = runner.run();
        stat.output("BCBDC账户模式", OUT_PATH);
    }
    
    public static void runBCBDCUTXO() throws FileNotFoundException {
        var runner = new RunBCBDCUTXO(config);
        var stat = runner.run();
        stat.output("BCBDC UTXO模式", OUT_PATH);
    }
    
    public static void runMixed() {
        var runner = new RunMixed(config);
        var stat = runner.run();
        stat.print("Mixed模式");
    }
    public static void runPureAccount() throws FileNotFoundException {
        var runner = new RunPureAccount(config);
        var stat = runner.run();
        stat.output("纯账户模式", OUT_PATH);
    }
    public static void runUTXO() throws FileNotFoundException {
        var runner = new RunUTXO(config);
        var stat = runner.run();
        stat.output("纯UTXO模式", OUT_PATH);
    }
}
