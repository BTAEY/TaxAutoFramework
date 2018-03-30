package com.ey.tax.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Created by zhuji on 2/6/2018.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static final Logger logger = LogManager.getLogger();

    private static ApplicationContext context;

    private ApplicationContextProvider(){}

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    public static <T> T getBean(String name,Class<T> tClass){
        if(context != null){
            return context.getBean(name,tClass);
        }
        return null;
    }

    public static <T> T getBean(Class<T> tClass){
        return getApplicationContext().getBean(tClass);
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        logger.debug("========ApplicationContext config success ,在普通类可以通过调用ApplicationContextProvider.getApplicationContext()获取applicationContext对象,ApplicationContext="+ApplicationContextProvider.getApplicationContext()+"========");
    }
}
