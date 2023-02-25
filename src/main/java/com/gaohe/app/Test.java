package com.gaohe.app;

import com.gaohe.app.service.UserService;
import com.gaohe.spring.GHApplicationContext;

public class Test {
    public static void main(String[] args) {
        GHApplicationContext applicationContext = new GHApplicationContext(AppConfig.class);

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
