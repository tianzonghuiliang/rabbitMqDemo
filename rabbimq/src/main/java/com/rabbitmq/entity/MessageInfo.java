package com.rabbitmq.entity;

import lombok.Data;

import java.util.Date;

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

    private Date sendTime;

    private long time;

    private Date receiveTime;
}
