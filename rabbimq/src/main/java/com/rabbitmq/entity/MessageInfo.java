package com.rabbitmq.entity;

import lombok.Data;

/**
 * Created by star on 2019/11/15.
 */
@Data
public class MessageInfo {
    //全局唯一id
    private String id;

    //消息
    private String msg;

    private int count;
}
