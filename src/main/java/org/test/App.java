package org.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.test.ConfigBean.CustomerConfiguration;

import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomerConfiguration.class);

        final ConfigBean configBean = (ConfigBean) context.getBean("configBean");

        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(configBean);
        }
    }
}
