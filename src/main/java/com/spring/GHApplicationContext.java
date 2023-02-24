package com.spring;

public class GHApplicationContext {

    private Class configClass;

    public GHApplicationContext(Class configClass){
        this.configClass=configClass;

        //解析配置类

    }

    public Object getBean(String beanName){
        return null;
    }
}
