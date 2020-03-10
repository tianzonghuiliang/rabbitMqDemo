package com.rabbitmq.producter;

import com.rabbitmq.entity.MessageInfo;
import com.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.utils.RedisLock;
import com.rabbitmq.utils.SnowIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by star on 2019/11/15.
 */
@RestController
@RequestMapping("producter")
@Slf4j
public class Producter {

    private ExecutorService executorService = Executors.newFixedThreadPool(100);
      @Autowired
      private RabbitMqUtils rabbitMqUtils;

      @PostMapping("/product")
      public String test(@RequestBody MessageInfo msgInfo){
          String id = SnowIDUtil.getId();
          String requestId = SnowIDUtil.getId();
          msgInfo.setId(id);
          msgInfo.setSendTime(new Date());
          //加锁
          Boolean flag = RedisLock.lock(id,requestId);
          if(flag) {
              rabbitMqUtils.send(msgInfo);
              //解锁
              RedisLock.unLock(id,requestId);
          }
          return "ok";
      }
}
