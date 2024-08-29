package com.lb.middleware.dynamic.thread.pool.sdk.config;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态配置入口
 *
 * @author RainSoul
 * @create 2024-08-29
 */
@Configuration
public class DynamicThreadPoolAutoConfig {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    @Bean("dynamicThreadPoolService")
    public String dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {

        threadPoolExecutorMap.keySet().forEach(key -> {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(key);
            System.out.println("threadPoolExecutor = " + threadPoolExecutor);
            int poolSize = threadPoolExecutor.getPoolSize();
            int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
            int corePoolSize = threadPoolExecutor.getCorePoolSize();
            BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
            String simpleName = queue.getClass().getSimpleName();
        });

        logger.info("线程池信息：{}", JSON.toJSONString(threadPoolExecutorMap.keySet()));

        return new String();
    }

}
