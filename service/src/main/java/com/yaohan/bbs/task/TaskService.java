package com.yaohan.bbs.task;

import com.yaohan.bbs.elastic.ESOperatorService;
import com.yaohan.bbs.service.PostsServcie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class TaskService {

    @Autowired
    ESOperatorService esOperatorService;

    @Autowired
    PostsServcie postsServcie;

    /**
     * 默认是fixedDelay 上一次执行完毕时间后执行下一轮
     * @Async
     * @Scheduled(cron = "0/5 * * * * *")
     *
     * fixedRate:上一次开始执行时间点之后5秒再执行
     * @Async
     * @Scheduled(fixedRate = 5000)

     * fixedDelay:上一次执行完毕时间点之后5秒再执行
     * @Async
     * @Scheduled(fixedDelay = 5000)

     * 第一次延迟2秒后执行，之后按fixedDelay的规则每5秒执行一次
     * @Async
     * @Scheduled(initialDelay = 2000, fixedDelay = 5000)
     *
     */
    @Async
    @Scheduled(cron = "0 0 0/1 * * *")
    public void run() throws InterruptedException{
        log.info("定时任务执行···");
    }
}
