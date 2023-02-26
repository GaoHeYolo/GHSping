package com.gaohe.app.service;

import com.gaohe.spring.annotation.GHComponent;
import com.gaohe.spring.annotation.GHScope;
import com.gaohe.spring.interfacer.GHBeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@GHComponent("gaoheBeanPostProcessor")
@GHScope("prototype")
public class GaoheBeanPostProcessor implements GHBeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
//        if (beanName.equals("userService")) {
//            System.out.println("初始化前");
//            ((UserServiceImpl) bean).setName("高赫确实帅");
//        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        //匹配
        if ("userService".equals(beanName)){
            Object proxyInstance = Proxy.newProxyInstance(GaoheBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");//找切点
                    return method.invoke(bean,args);
                }
            });

            return proxyInstance;
        }
        return bean;
    }
}
