package com.lb.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author RainSoul
 * @create 2024-08-29
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {

    @Bean("threadPoolExecutor01")
    public ThreadPoolExecutor threadPoolExecutor01(ThreadPoolConfigProperties threadPoolConfigProperties) {
        /**
         * RejectedExecutionHandler 是 ThreadPoolExecutor 的一个接口，用于处理当任务被拒绝执行时的情形。
         * 当线程池中的队列已满且没有空闲线程时，新提交的任务将被拒绝，此时 RejectedExecutionHandler 接口的实现类将决定如何处理这种情况。
         */
        RejectedExecutionHandler handler;
        switch (threadPoolConfigProperties.getPolicy()) {
            case "AbortPolicy":
                handler = new ThreadPoolExecutor.AbortPolicy(); // 抛出 RejectedExecutionException。
                break;
            case "DiscardOldestPolicy":
                handler = new ThreadPoolExecutor.DiscardOldestPolicy(); //丢弃队列中最老的任务，然后尝试再次提交被拒绝的任务。
                break;
            case "DiscardPolicy":
                handler = new ThreadPoolExecutor.DiscardPolicy();   // 直接丢弃被拒绝的任务。
                break;
            case "CallerRunsPolicy":
                handler = new ThreadPoolExecutor.CallerRunsPolicy();    //由调用者线程运行该任务。
            default:
                handler = new ThreadPoolExecutor.AbortPolicy();
        }

        return new ThreadPoolExecutor(
                threadPoolConfigProperties.getCorePoolSize(),   //线程池的核心线程数。
                threadPoolConfigProperties.getMaxPoolSize(),    //线程池的最大线程数。
                threadPoolConfigProperties.getKeepAliveTime(),  //非活动线程在终止前等待新任务的最长时间。
                TimeUnit.SECONDS,                               //参数的时间单位。
                new LinkedBlockingQueue<>(threadPoolConfigProperties.getBlockQueueSize()),  //线程池所使用的阻塞队列，用于存放等待执行的任务。
                Executors.defaultThreadFactory(),   //默认的线程工厂。
                handler);   //根据策略设置的拒绝执行处理器。

    }

    @Bean("threadPoolExecutor02")
    public ThreadPoolExecutor threadPoolExecutor02(ThreadPoolConfigProperties properties) {
        // 实例化策略
        RejectedExecutionHandler handler;
        switch (properties.getPolicy()) {
            case "AbortPolicy":
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "DiscardPolicy":
                handler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case "CallerRunsPolicy":
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            default:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }

        // 创建线程池
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                handler);
    }
}
