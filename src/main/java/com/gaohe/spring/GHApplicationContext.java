package com.gaohe.spring;

import com.gaohe.spring.annotation.GHAutowired;
import com.gaohe.spring.annotation.GHComponent;
import com.gaohe.spring.annotation.GHComponentScan;
import com.gaohe.spring.annotation.GHScope;
import com.gaohe.spring.interfacer.GHBeanNameAware;
import com.gaohe.spring.interfacer.GHInitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GHApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();//单例池
    private ConcurrentHashMap<String, GHBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public GHApplicationContext(Class configClass) {
        this.configClass = configClass;

        //解析配置类
        //GHComponentScan注解--->扫描路径--->扫描--->BeanDefinition--->BeanDefinitionMap
        scan(configClass);

        for (Map.Entry<String, GHBeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            GHBeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    public Object createBean(String beanName, GHBeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(GHAutowired.class)) {
                    Object bean = getBean(declaredField.getName());
                    GHAutowired declaredAnnotation = declaredField.getDeclaredAnnotation(GHAutowired.class);
                    boolean required = declaredAnnotation.required();
                    if (bean == null && required) {
                        throw new NullPointerException();
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }
            //Aware回调
            if (instance instanceof GHBeanNameAware) {
                ((GHBeanNameAware) instance).setBeanName(beanName);
            }
            //初始化
            if (instance instanceof GHInitializingBean) {
                try {
                    ((GHInitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scan(Class configClass) {
        GHComponentScan componentScanAnnotation = (GHComponentScan) configClass.getDeclaredAnnotation(GHComponentScan.class);
        String path = componentScanAnnotation.value().replace(".", "/");//扫描路径
        //扫描
        //Bootstrap--->jre/lib
        //Ext--------->jre/ext/lib
        //App--------->classpath---->
        ClassLoader classLoader = GHApplicationContext.class.getClassLoader();//app
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class")).replace("\\", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(GHComponent.class)) {
                            //表示当前这个类是一个Bean
                            //解析类，判断当前备案是单例备案，还是prototype的bean
                            //BeanDefinition--->Bean的定义
                            GHComponent componentAnnotation = clazz.getDeclaredAnnotation(GHComponent.class);
                            String beanName = componentAnnotation.value();

                            GHBeanDefinition beanDefinition = new GHBeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if (clazz.isAnnotationPresent(GHScope.class)) {
                                GHScope scopeAnnotation = clazz.getDeclaredAnnotation(GHScope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            GHBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                Object o = singletonObjects.get(beanName);
                return o;
            } else {
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }
        } else {
            //不存在对应的Bean
            throw new NullPointerException();
        }
    }
}
