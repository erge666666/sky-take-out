package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
@Slf4j
public class task1 {

    //@Scheduled(cron = "0/5 * * * * ? ")
    public void text1(){
        //每5秒执行一次
        log.info("{}执行一次",LocalDateTime.now());
    }

}
