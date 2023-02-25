package com.gaohe.spring.interfacer;

public interface GHBeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean,String beanName);

    Object postProcessAfterInitialization(Object bean,String beanName);
}

