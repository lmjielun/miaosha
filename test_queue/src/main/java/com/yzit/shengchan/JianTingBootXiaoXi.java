package com.yzit.shengchan;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *  监听
 */
@Component
public class JianTingBootXiaoXi {
    // 监听哪个消息？
    @JmsListener(destination = "delay.queue.web")
    public void JianTing(String message){
        System.out.println("接收到的消息是"+message);
    }
}
