package com.lb.middleware.dynamic.thread.pool.sdk.trigger.job;

import com.alibaba.fastjson2.JSON;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.lb.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author RainSoul
 * @create 2024-08-29
 */
public class ThreadPoolDataReportJob {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    /**
     * "0/20 * * * * ?" 表示这个方法将在每个小时的第0分钟和第20分钟执行，即每小时执行两次，分别在整点和20分时执行。
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void execReportThreadPoolList() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池信息：{}", threadPoolConfigEntities);

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池信息：{}", JSON.toJSONString(threadPoolConfigEntity));
        }
    }
}
