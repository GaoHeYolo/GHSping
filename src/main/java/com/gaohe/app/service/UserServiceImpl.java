package com.gaohe.app.service;

import com.gaohe.spring.annotation.GHAutowired;
import com.gaohe.spring.annotation.GHComponent;
import com.gaohe.spring.interfacer.GHInitializingBean;

@GHComponent("userService")
public class UserServiceImpl implements UserService {

    @GHAutowired
    private OrderService orderService;

    private String name;

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void test(){
        System.out.println(orderService);
        System.out.println(name);
    }
}
