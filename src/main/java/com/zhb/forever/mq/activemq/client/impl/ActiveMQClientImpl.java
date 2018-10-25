package com.zhb.forever.mq.activemq.client.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.zhb.forever.framework.proto.ProtoResult;
import com.zhb.forever.framework.proto.support.ProtoConverter;
import com.zhb.forever.mq.activemq.client.ActiveMQClient;
/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月24日下午3:01:53
*/

public class ActiveMQClientImpl implements ActiveMQClient {

    private Logger logger = LoggerFactory.getLogger(ActiveMQClientImpl.class);
    
    private JmsTemplate jmsQueueTemplate;
    
    private JmsTemplate jmsTopicTemplate;
    

    /*------Quene begin---------------------------------------*/
    public void sendQueueDestinationMsg(Destination destination, final String msg) {
        jmsQueueTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }
    
    public void sendQueueDestinationNameMsg(String destinationName, final String msg) {
        jmsQueueTemplate.send(destinationName, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }
    
    public void sendQueueMessage(final String msg) {
        jmsQueueTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }
    
    public void sendQueueRemoteMsg(String destinationName, byte[] msg) {
        ProtoResult pr = new ProtoResult();
        pr.setProtoBytes(msg);
        jmsQueueTemplate.send(destinationName, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(pr);
            }
        });
    }

    public TextMessage receiveQueueMessage(Destination destination) {
        Message m = jmsQueueTemplate.receive(destination);
        TextMessage tm = null;
        if (null != m) {
            tm = (TextMessage)m;
        }
        return tm;
    }
    
    public TextMessage receiveQueueMessage(String destinationName) {
        Message m = jmsQueueTemplate.receive(destinationName);
        TextMessage tm = null;
        if (null != m) {
            tm = (TextMessage)m;
        }
        return tm;
    }
    
    public com.google.protobuf.Message receiveQueueRemoteMsgByDesNamePath(String destinationName,String path) throws Exception {
        Message m = jmsQueueTemplate.receive(destinationName);
        if (null == m) {
            return null;
        }
        ObjectMessage om  = (ObjectMessage)m;
        Object object = om.getObject();
        if (null == object) {
            return null;
        }
        ProtoResult pr = (ProtoResult)object;
        ProtoConverter pci = new ProtoConverter();
        return pci.converFromProto(path, pr);
    }
    
/*------Quene end---------------------------------------*/

    
    
/*------Topic begin---------------------------------------*/
    @Override
    public void sendTopicMessage(String destinationName, String msg) {
        jmsTopicTemplate.send(destinationName, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

    @Override
    public TextMessage receiveTopicMessage(String destinationName) {
        Message m = jmsTopicTemplate.receive(destinationName);
        TextMessage tm = null;
        if (null != m) {
            tm = (TextMessage)m;
        }
        return tm;
    }

    /*------Topic end---------------------------------------*/
    
    
    
    public JmsTemplate getJmsQueueTemplate() {
        return jmsQueueTemplate;
    }

    public void setJmsQueueTemplate(JmsTemplate jmsQueueTemplate) {
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    public JmsTemplate getJmsTopicTemplate() {
        return jmsTopicTemplate;
    }

    public void setJmsTopicTemplate(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }   

}


