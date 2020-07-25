package com.yzit.shengchan;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BootXiaoFei {

    @JmsListener(destination = "delay.queue.web")
    public void receive(String message) {
        System.out.println("boot收到的 message 是：" + message);
    }
}
