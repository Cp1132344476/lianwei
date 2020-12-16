package com.lianwei.config;

import com.lianwei.properties.Property;
import com.lianwei.service.TestService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Property.class)
public class SplitAutoConfiguration {
    private final Property property;

    public SplitAutoConfiguration(Property property) {
        this.property = property;
    }

    @Bean
    @ConditionalOnMissingBean
    public TestService testService() {
        return new TestService();
    }
}
