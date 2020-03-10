package com.rabbitmq.consumer;

import com.rabbitmq.client.*;

import com.rabbitmq.config.DelayMqConfig;

import com.rabbitmq.entity.MessageInfo;
import com.rabbitmq.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：xjm
 * @ Date       ：Created in 9:22 2019/6/23
 * @ Description：${description}
 */
@Component
@Slf4j
public class DelayConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DelayMqConfig.DEAL_QUEUE)
    public void onMessage(String msg, Message message, Channel channel) throws Exception {

       MessageInfo msgInfo = JSONUtils.toBean(msg,MessageInfo.class);
       msgInfo.setReceiveTime(new Date());
        try {

            log.info("收到消息：{}",msgInfo);
            /** 手动抛出异常,测试消息重试 */
           // int i = 1 / 0;

        } catch (Exception e) {
            int retryCount = msgInfo.getCount();
            if (retryCount >= 3) {

                //重试次数超过3次,则将消息发送到失败队列等待特定消费者处理或者人工处理
                try {
                    rabbitTemplate.convertAndSend(DelayMqConfig.FAIL_EXCHANGE,DelayMqConfig.FAIL_ROUTING_KEY, msg);
                    log.info("重试3次后依然失败，将消息发送到fail队列:" + new String(message.getBody()));
                } catch (Exception e1) {
                    log.error("发送到fail队列报错:" + e1.getMessage() + ",原始消息:"+ new String(message.getBody()));
                }

            } else {
                Thread.sleep(1000);
                //次数加1
                retryCount++;
                msgInfo.setCount(retryCount);
                try {
                    //重试次数不超过3次,则将消息发送到重试队列等待重新被消费（重试队列延迟超时后信息被发送到相应死信队列重新消费，即延迟消费
                    rabbitTemplate.convertAndSend(DelayMqConfig.DEAL_EXCHANGE, DelayMqConfig.DEAL_ROUTING_KEY, JSONUtils.toJson(msgInfo));
                    log.info("发送到重试队列;" + "原始消息：" + new String(message.getBody()) + ";第" + (retryCount) + "次重试");
                } catch (Exception e1) {
                    log.error("发送到重试队列异常:" + e1.getMessage() + ",重新发送消息");
                }
            }
        } finally {
             //无论消费成功还是消费失败,都要手动进行ack,因为即使消费失败了,也已经将消息重新投递到重试队列或者失败队列
             //如果不进行ack,生产者在超时后会进行消息重发,如果消费者依然不能处理，则会存在死循环

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }







}
