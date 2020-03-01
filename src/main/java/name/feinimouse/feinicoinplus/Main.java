package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.SimRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    
    public static void main(String[] args) {

        // Base test
        ApplicationContext context = new AnnotationConfigApplicationContext(BaseRunner.class);
        SimRunner runner = (SimRunner) context.getBean("simRunner");
        runner.start(null);
    }
}
