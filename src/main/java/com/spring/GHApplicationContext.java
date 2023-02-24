package com.spring;

public class GHApplicationContext {

    private Class configClass;

    public GHApplicationContext(Class configClass){
        this.configClass=configClass;

        //解析配置类
        //GHComponentScan注解--->扫描路径--->扫描

    }

    public Object getBean(String beanName){
        return null;
    }
}
