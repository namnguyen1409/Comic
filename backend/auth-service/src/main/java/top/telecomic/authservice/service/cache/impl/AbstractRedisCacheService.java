package top.telecomic.authservice.service.cache.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import top.telecomic.authservice.service.cache.CacheService;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public abstract class AbstractRedisCacheService<K, V> implements CacheService<K, V> {

    RedisTemplate<String, V> redisTemplate;

    protected abstract String buildCacheKey(K key);

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(buildCacheKey(key));
    }

    @Override
    public void put(K key, V value) {
        redisTemplate.opsForValue().set(buildCacheKey(key), value);
    }

    @Override
    public void evict(K key) {
        redisTemplate.delete(buildCacheKey(key));
    }

    @Override
    public boolean exists(K key) {
        return redisTemplate.hasKey(buildCacheKey(key));
    }
}
