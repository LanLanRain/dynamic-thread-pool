package com.lb.middleware.dynamic.thread.pool.sdk.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.lb.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 动态线程池变更监听
 *
 * @author RainSoul
 * @create 2024-08-29
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {
    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    /**
     * 当接收到消息时会被调用。这个方法接收两个参数：charSequence是一个字符序列，而threadPoolConfigEntity是一个线程池配置实体对象。
     * 监听线程池配置的变更消息，更新线程池配置，并上报最新的配置信息到注册中心。
     * @param charSequence
     * @param threadPoolConfigEntity
     */
    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}",
                threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getPoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize());
        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        //查询当前所有的线程池配置列表。
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        //上报最新的线程池列表到注册中心。
        registry.reportThreadPool(threadPoolConfigEntities);
        //查询当前的线程池配置实体。
        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.queryThreadPooConfigByName(threadPoolConfigEntity.getThreadPoolName());
        //上报当前线程池的配置参数到注册中心。
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
    }
}
