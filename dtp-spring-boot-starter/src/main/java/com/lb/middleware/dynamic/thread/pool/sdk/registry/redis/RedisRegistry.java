package com.lb.middleware.dynamic.thread.pool.sdk.registry.redis;

import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.lb.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import com.lb.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;

/**
 * 注册中心
 * @author RainSoul
 * @create 2024-08-29
 */
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 用于报告线程池配置信息。它首先获取一个名为threadPoolConfigList的列表，然后删除该列表中的所有元素，最后将新的线程池配置实体添加到列表中。
     * @param threadPoolConfigEntityList
     */
    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntityList) {
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.delete();
        list.addAll(threadPoolConfigEntityList);
    }

    /**
     * 构建了一个缓存键，该键由线程池配置参数的键、应用名称和线程池名称组成。
     * @param threadPoolConfigEntity
     */
    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" +
                threadPoolConfigEntity.getAppName() + "_" +
                threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
