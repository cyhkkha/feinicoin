package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.SimRunner;
import name.feinimouse.feinicoinplus.core.sim.ResultManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static final String ENV_TEST_BASE = "TEST_BASE";
    public static final String ENV_TEST = "TEST";
    
    public static void main(String[] args) {
//        String ENV = ENV_TEST_BASE;
//        ResultManager resultM;
//        
//        if (ENV.equals(ENV_TEST_BASE)) {
//            ApplicationContext context = new AnnotationConfigApplicationContext(BaseTestConfig.class);
//            SimRunner runner = (SimRunner) context.getBean("simRunner");
//            resultM = runner.start(new InitParam());
//        }
    }
}
