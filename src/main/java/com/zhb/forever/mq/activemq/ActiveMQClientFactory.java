package com.zhb.forever.mq.activemq;

import javax.jms.Destination;

import com.zhb.forever.framework.spring.bean.locator.SpringBeanLocator;
import com.zhb.forever.mq.Constants;
import com.zhb.forever.mq.activemq.client.ActiveMQClient;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月24日下午3:13:33
*/

public class ActiveMQClientFactory {

    public static ActiveMQClient getRedisClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.ACTIVE_MQ_CLIENT_CONF).getBean(
                        Constants.ACTIVE_MQ_CLIENT);
        return (ActiveMQClient) bean;
    }
    
    public static Destination getMQDestinationBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.ACTIVE_MQ_CLIENT_CONF).getBean(
                        Constants.ACTIVE_MQ_DEFAULT_DESTINATION);
        return (Destination) bean;
    }

}


