package com.rabbitmq.utils;

public class SnowIDUtil {

    private static SnowFlake snowFlake = new SnowFlake(1);

    public static String getId() {
        Long id = snowFlake.nextId();
        return String.valueOf(id);
    }

    public static void main(String[] args) {
        SnowIDUtil snowIDUtil = new SnowIDUtil();
        System.out.println(snowIDUtil.getId());
    }
}
