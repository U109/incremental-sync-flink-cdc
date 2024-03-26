package com.zzz.flink.flinkcdcconsumer.config;

import com.rabbitmq.client.Channel;
import com.zzz.flink.flinkcdcconsumer.util.MessageUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangzhongzhen wrote on 2024/3/10
 * @version 1.0
 * @description:
 */
@RabbitListener(queues = {"flink_cdc_exchange_consumer.queue"})
@Component
public class FlinkListener {


    @Autowired
    FlinkQueueListenerConfig flinkQueueListenerConfig;

    @RabbitHandler
    public void messagerevice(String msg, Channel channel, Message message) throws IOException {
        try {
            System.out.println("msg : " + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //获取通讯的消息，组装成对应的监听对象
            List<String> queues = MessageUtils.disposeMessage(msg);
            flinkQueueListenerConfig.registerListener(queues);
        } catch (Exception e) {
            //重试消费，或投递到死信队列
            //注意：参数三是否运行重试，若设置为true，会出现死循环，你可以定义常量设置重试次数
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }
    }
}