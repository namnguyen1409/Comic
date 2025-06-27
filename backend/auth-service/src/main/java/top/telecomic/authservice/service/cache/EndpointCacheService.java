package top.telecomic.authservice.service.cache;

import top.telecomic.authservice.dto.cache.EndpointCache;

import java.util.List;

public interface EndpointCacheService extends CacheService<String, List<EndpointCache>> {

    void sync(String serviceName);

}
