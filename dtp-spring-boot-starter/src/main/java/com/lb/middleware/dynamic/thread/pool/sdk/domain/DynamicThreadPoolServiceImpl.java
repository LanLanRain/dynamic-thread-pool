package com.lb.middleware.dynamic.thread.pool.sdk.domain;

import com.alibaba.fastjson.JSON;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态线程池服务
 *
 * @author RainSoul
 * @create 2024-08-29
 */
public class DynamicThreadPoolServiceImpl implements IDynamicThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolServiceImpl.class);

    private final String applicationName;
    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolServiceImpl(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        Set<String> threadPoolBeanNames = threadPoolExecutorMap.keySet();
        List<ThreadPoolConfigEntity> threadPoolConfigVOS = new ArrayList<>(threadPoolBeanNames.size());
        for (String threadPoolBeanName : threadPoolBeanNames) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolBeanName);
            ThreadPoolConfigEntity threadPoolConfigVO = getThreadPoolConfigVO(threadPoolExecutor);
            threadPoolConfigVOS.add(threadPoolConfigVO);
        }
        return threadPoolConfigVOS;
    }

    private static ThreadPoolConfigEntity getThreadPoolConfigVO(ThreadPoolExecutor threadPoolExecutor) {
        ThreadPoolConfigEntity threadPoolConfigVO = new ThreadPoolConfigEntity();
        threadPoolConfigVO.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolConfigVO.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolConfigVO.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolConfigVO.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolConfigVO.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolConfigVO.setQueueType(threadPoolExecutor.getQueue().getClass().getName());
        threadPoolConfigVO.setRemainingCapacity(threadPoolConfigVO.getRemainingCapacity());
        return threadPoolConfigVO;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPooConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (threadPoolExecutor == null) {
            return new ThreadPoolConfigEntity(applicationName, threadPoolName);
        }
        ThreadPoolConfigEntity threadPoolConfigVO = getThreadPoolConfigVO(threadPoolExecutor);

        if (logger.isDebugEnabled()) {
            logger.info("动态线程池，配置查询 应用名:{} 线程名:{} 池化配置:{}", applicationName, threadPoolName, JSON.toJSONString(threadPoolConfigVO));
        }

        return threadPoolConfigVO;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        if (threadPoolConfigEntity == null || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (threadPoolExecutor == null) return;
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }
}
