package cn.aorise.common.core.cache;

/**
 * 缓存接口
 */
public interface ICache {
    void put(String key, Object value);

    Object get(String key);

    boolean contains(String key);

    void remove(String key);

    void clear();
}