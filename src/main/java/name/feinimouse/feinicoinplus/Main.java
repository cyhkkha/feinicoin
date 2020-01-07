package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.data.InitParam;
import name.feinimouse.feinicoinplus.core.SimRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("spring-feinicoin-test.xml");
        SimRunner runner = (SimRunner) context.getBean("runner");
        runner.start(new InitParam());
    }
}
