package top.telecomic.authservice.service.cache;

public interface CacheService<K, V> {

    V get(K key);

    void put(K key, V value);

    void evict(K key);

    boolean exists(K key);

}
