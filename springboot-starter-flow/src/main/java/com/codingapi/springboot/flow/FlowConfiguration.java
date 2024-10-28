package com.codingapi.springboot.flow;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public FlowSessionBeanRegister flowHolderRegister(ApplicationContext spring) {
        return new FlowSessionBeanRegister(spring);
    }

}