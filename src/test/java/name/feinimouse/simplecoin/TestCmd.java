package name.feinimouse.simplecoin;

import org.junit.Test;

public class TestCmd {
    @Test
    public void testUTXO() {
        String[] args = { "-utxo", "-utxo_size", "8", "-user_count", "99", "-bundle_size", "18", "-asset_rate", "3.4" };
        var cli = new Cli(args);
        var config = cli.parser();
        System.out.println(config);
    }
    @Test
    public void testTestCount() {
        String[] args = { "-b", "-test_count", "1,2,3,4" };
        var cli = new Cli(args);
        var config = cli.parser();
        System.out.println(config);
    }

    @Test
    public void testBaseInfo() {
        String[] args = { "-b", "-net" };
        Main.main(args);
    }
}
