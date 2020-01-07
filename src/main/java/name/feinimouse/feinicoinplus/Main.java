package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static final String ENV_TEST_BASE = "TEST_BASE";
    public static final String ENV_TEST = "TEST";
    
    @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions", "UnusedAssignment"})
    public static void main(String[] args) {
        String ENV = ENV_TEST_BASE;
        ResultManager resultM;
        
        if (ENV.equals(ENV_TEST_BASE)) {
            ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-feinicoin-test.xml");
            SimRunner runner = (SimRunner) context.getBean("runner");
            resultM = runner.start(new InitParam());
        }
    }
}
