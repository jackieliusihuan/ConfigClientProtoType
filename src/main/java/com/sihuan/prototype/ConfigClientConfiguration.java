package com.sihuan.prototype;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@ComponentScan("com.sihuan.prototype")
public class ConfigClientConfiguration {

    @Bean
    public ConfigClient configClient() {
        return new StubConfigClient();
    }

    /**
     * 出于测试目的，这里提供一个stub来提供ConfigClient的功能
     */
    public static class StubConfigClient implements ConfigClient {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public String getConfig(String configId) {
            if ("testConfig".equals(configId)) {
                return counter.addAndGet(1) / 5 % 2 == 0 ?
                        "name=bob\n" +
                                "job=developer\n" +
                                "company=google\n" +
                                "salary=99999" :
                        "name=jack\n" +
                                "job=sales\n" +
                                "company=apple\n" +
                                "salary=88888";
            }
            throw new RuntimeException();
        }
    }
}
