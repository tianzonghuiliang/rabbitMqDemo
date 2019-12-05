package com.rabbitmq.utils;

import com.rabbitmq.config.DelayMqConfig;
import com.rabbitmq.entity.MessageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ Author     ：xjm
 * @ Date       ：Created in 8:54 2019/6/23
 * @ Description：${description}
 */
@Component
@Slf4j
public class RabbitMqUtils {




    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Autowired
//    private AmqpTemplate amqpTemplate;
//
//    @Bean
//    public AmqpTemplate amqpTemplate(){
//        // 在消息没有被路由到合适的队列情况下，Broker会将消息返回给生产者，
//        // 为true时如果Exchange根据类型和消息Routing Key无法路由到一个合适的Queue存储消息，
//        // Broker会调用Basic.Return回调给handleReturn()，再回调给ReturnCallback，将消息返回给生产者。
//        // 为false时，丢弃该消息
//        rabbitTemplate.setMandatory(true);
//        // 消息确认，需要配置 spring.rabbitmq.publisher-confirms = true
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//
//            //根据返回的状态，生产者可以处理失败与成功的相应信息，比如发送失败，可重发，转发或者存入日志等
//            //if(ack){
//            //	correlationData.getId()为message唯一标识，需要生产者发送message时传入自定义的correlationData才能获取到，否则为null
//            //	//do something
//            //}else{
//            //	correlationData.getId()
//            //	//do something
//            //}
//            //此处只做打印，不对生产者发送失败的信息处理
//            System.out.println("------------------------------------------------");
//            System.out.println("ConfirmCallBackListener：correlationData=" + correlationData + "，ack=" + ack + "，cause=" + cause);
//            System.out.println("------------------------------------------------");
//
//        });
//
//        // 消息发送失败返回到队列中，需要配置spring.rabbitmq.publisher-returns = true
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//
//            System.out.println("------------------------------------------------");
//            System.out.println("ReturnCallBackListener：message=" + new String(message.getBody()) + "，replyCode=" + replyCode + "，replyText=" + replyText + "，exchange=" + exchange + "，routingKey=" + routingKey);
//            System.out.println("------------------------------------------------");
//
//        });
//
//        return rabbitTemplate;
//    }


    public void send(MessageInfo msgInfo) {

        log.info("消息：{}", new Date() + "=" + msgInfo);

        MessagePostProcessor processor = new MessagePostProcessor(){
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
               // message.getMessageProperties().setExpiration(30*1000 + "");
                return message;
            }
        };
        rabbitTemplate.convertAndSend(DelayMqConfig.DELAY_EXCHANGE,DelayMqConfig.DELAY_ROUTING_KEY, JSONUtils.toJson(msgInfo));
        log.info("[发送时间] - [{}]", LocalDateTime.now());
    }




}
