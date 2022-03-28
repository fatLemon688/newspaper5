package com.bigotry.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/5 0:17
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public final class ContextUtil implements ApplicationContextAware {
    protected static ApplicationContext applicationContext ;
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
            applicationContext=arg0;
    }
    public static Object getBean(String name) {
        //name表示其他要注入的注解name名
        return applicationContext.getBean(name);
    }
    public static ApplicationContext getContext() {
        return applicationContext;
    }
}
