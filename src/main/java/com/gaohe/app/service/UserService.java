package com.gaohe.app.service;

import com.gaohe.spring.annotation.GHAutowired;
import com.gaohe.spring.annotation.GHComponent;
import com.gaohe.spring.interfacer.GHInitializingBean;

@GHComponent("userService")
public class UserService implements GHInitializingBean {

    @GHAutowired
    private OrderService orderService;

    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化");
    }
}
