package com.yzit.shengchan;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.springframework.scheduling.annotation.Scheduled;

import javax.jms.*;

public class ShengChan {
    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.获取session  (参数1：是否启动事务,参数2：消息确认模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Queue queue = session.createQueue("delay.queue"); // 名称起名为延迟队列，名称随意起
        //6.创建消息生产者，将生产者与session与队列进行绑定
        MessageProducer producer = session.createProducer(queue);
        //7.创建消息
        TextMessage textMessage = session.createTextMessage("延迟消息");
        // 设置延时发送 15秒后发送 “延迟消息” 字符串
        long time = 15 * 1000;
        textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);

        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}
