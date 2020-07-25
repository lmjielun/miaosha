package com.yzit.shengchan;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

@RestController
public class BootShengChan {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送消息队列 第二种方式
     */
    @GetMapping("sendQueue2")
    public String sendQueue2(){
        // 创建目标
        Destination destination = new ActiveMQQueue("delay.queue.web");
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // 使用session创建文本消息
                TextMessage textMessage = session.createTextMessage("yanchixiaoxi");
                // 设置延迟
                long time = 15 * 1000;
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
                return textMessage;
            }
        });


        return "成功2";
    }
}
