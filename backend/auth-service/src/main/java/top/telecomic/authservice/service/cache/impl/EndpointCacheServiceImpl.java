package top.telecomic.authservice.service.cache.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.telecomic.authservice.dto.cache.EndpointCache;
import top.telecomic.authservice.mapper.EndpointMapper;
import top.telecomic.authservice.repository.EndpointRepository;
import top.telecomic.authservice.service.cache.EndpointCacheService;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EndpointCacheServiceImpl
        extends AbstractRedisCacheService<String, List<EndpointCache>>
        implements EndpointCacheService {
    private final EndpointMapper endpointMapper;

    private final EndpointRepository endpointRepository;

    public EndpointCacheServiceImpl(RedisTemplate<String, List<EndpointCache>> redisTemplate, EndpointRepository endpointRepository,
                                    EndpointMapper endpointMapper) {
        super(redisTemplate);
        this.endpointRepository = endpointRepository;
        this.endpointMapper = endpointMapper;
    }

    @Override
    protected String buildCacheKey(String key) {
        return "endpoints:" + key;
    }


    @Override
    @Async
    public void sync(String serviceName) {
        try {
            var endpoints = endpointRepository.findByServiceName(serviceName);
            evict(serviceName);
            if (endpoints.isEmpty()) {
                log.info("No endpoints found for service: {}, cache evicted", serviceName);
                return;
            }
            var endpointCaches = endpoints.stream().map(endpointMapper::toCacheDto).toList();
            put(serviceName, endpointCaches);
        } catch (Exception e) {
            log.error("Failed to sync cache for service: {}", serviceName, e);
        }

    }
}
