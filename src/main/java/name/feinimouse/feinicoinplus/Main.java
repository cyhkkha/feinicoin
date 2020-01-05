package name.feinimouse.feinicoinplus;

import name.feinimouse.feinicoinplus.core.HashGenerator;
import name.feinimouse.feinicoinplus.core.node.CenterContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("spring-feinicoin-test.xml");
        CenterContext centerContext = (CenterContext) context.getBean("centerContext");
        HashGenerator hashGenerator = (HashGenerator) context.getBean("hashGenerator");
    }
}
