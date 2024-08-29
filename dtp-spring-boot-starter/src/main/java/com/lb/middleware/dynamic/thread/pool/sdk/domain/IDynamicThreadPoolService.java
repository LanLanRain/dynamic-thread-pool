package com.lb.middleware.dynamic.thread.pool.sdk.domain;

import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 动态线程池服务
 * @author RainSoul
 * @create 2024-08-29
 */
public interface IDynamicThreadPoolService {
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    ThreadPoolConfigEntity queryThreadPooConfigByName(String threadPoolName);

    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);
}
