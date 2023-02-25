package com.gaohe.app.service;

import com.gaohe.spring.annotation.GHComponent;
import com.gaohe.spring.annotation.GHScope;
import com.gaohe.spring.interfacer.GHBeanPostProcessor;

@GHComponent("gaoheBeanPostProcessor")
@GHScope("prototype")
public class GaoheBeanPostProcessor implements GHBeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("userService")) {
            System.out.println("初始化前");
            ((UserService) bean).setName("高赫确实帅");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        return bean;
    }
}
