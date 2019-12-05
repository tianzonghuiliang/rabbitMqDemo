package com.rabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Author     ：xjm
 * @ Date       ：Created in 9:09 2019/6/23
 * @ Description：${description}
 */
@Configuration
public class DelayMqConfig {

    /**
     * 延迟队列
     */
    public static final String DELAY_QUEUE = "delay.queue";
    /**
     *延迟交换机
     */
    public static final String DELAY_EXCHANGE = "delay.exchange";

    /**
     * 延迟队列routing key 名称
     */
    public static final String DELAY_ROUTING_KEY = "delay_routing_key";



    /**
     * 死信队列 名称
     */
    public  static final String DEAL_QUEUE = "deal.queue";
    /**
     * 死信交换机
     */
    public static final String DEAL_EXCHANGE = "deal.exchange";



    /**
     * 死信routing key 名称
     */
    public static final String DEAL_ROUTING_KEY = "deal_routing_key";



    /**
     * 失败队列 名称
     */
    public  static final String FAIL_QUEUE = "fail.queue";
    /**
     * 失败交换机
     */
    public static final String  FAIL_EXCHANGE = "fail.exchange";


    /**
     * 失败routing key 名称
     */
    public static final String  FAIL_ROUTING_KEY = "fail_routing_key";



    /**
     * 延迟队列配置
     * <p>
     * 1、params.put("x-message-ttl", 5 * 1000);
     * 第一种方式是直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活,（当然二者是兼容的,默认是时间小的优先）
     * 2、rabbitTemplate.convertAndSend(book, message -> {
     * message.getMessageProperties().setExpiration(2 * 1000 + "");
     * return message;
     * });
     * 第二种就是每次发送消息动态设置延迟时间,这样我们可以灵活控制
     **/
    @Bean
    public Queue delayQueue() {
        Map<String, Object> params = new HashMap<>();
//        params.put("x-max-length",10 );
//        //设置队列溢出方式    保留前10条
//        params.put("x-overflow","reject-publish" );
        // x-deal-letter-exchange 声明了队列里的死信转发到的DLX名称，
        // x-deal-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        params.put("x-message-ttl", 30000);
        params.put("x-dead-letter-exchange", DEAL_EXCHANGE);
        params.put("x-dead-letter-routing-key", DEAL_ROUTING_KEY);
//        params.put("x-dead-letter-exchange",DEAL_EXCHANGE);
//        params.put("x-dead-letter-routing-key",DEAL_ROUTING_KEY);
        return new Queue(DELAY_QUEUE, true, false, false, params);
    }

    /**
     * 延迟交换机
     * @return
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    /**
     * 延迟队列绑定
     * @return
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue dealQueue() {
        return new Queue(DEAL_QUEUE, true);
    }



    /**
     * 声明死信交换机
     * @return
     */
    @Bean
    public TopicExchange dealExchange() {
        return new TopicExchange(DEAL_EXCHANGE);
    }


    /**
     *死信队列绑定交换机
     * @return
     */
    @Bean
    public Binding dealBinding() {
        return BindingBuilder.bind(dealQueue()).to(dealExchange()).with(DEAL_ROUTING_KEY);
    }











    /**
     * 失败队列
     * @return
     */
    @Bean
    public Queue failQueue() {
        return new Queue(FAIL_QUEUE, true);
    }





    /**
     * 声明失败交换机
     * @return
     */
    @Bean
    public DirectExchange failExchange() {
        return new DirectExchange(FAIL_EXCHANGE);
    }


    /**
     *失败绑定交换机
     * @return
     */
    @Bean
    public Binding failBinding() {
        return BindingBuilder.bind(failQueue()).to(failExchange()).with(FAIL_ROUTING_KEY);
    }











}



