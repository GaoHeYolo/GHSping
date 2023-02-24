package com.gaohe;

import com.spring.GHApplicationContext;

public class Test {
    public static void main(String[] args) {
        GHApplicationContext applicationContext = new GHApplicationContext(AppConfig.class);

        Object userService = applicationContext.getBean("userService");
    }
}
