package cn.enilu.flash.cache.impl;

import cn.enilu.flash.cache.CacheDao;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * RedisCacheDao
 *
 * @author cyh
 * @version 2018/9/12 0012
 */
@Component
public class RedisCacheDao implements CacheDao {

    @Resource
    private CacheManager cacheManager;

    @Override
    public void hset(Serializable key, Serializable k, Object val) {
        Cache cache = cacheManager.getCache(String.valueOf(key));
        cache.put(k,val);
    }

    @Override
    public Serializable hget(Serializable key, Serializable k) {
        Cache cache = cacheManager.getCache(String.valueOf(key));
        return cache.get(k,String.class);

    }


    @Override
    public <T>T hget(Serializable key, Serializable k,Class<T> klass) {
        Cache cache = cacheManager.getCache(String.valueOf(key));
        return cache.get(k,klass);
    }
    @Override
    public void set(Serializable key, Object val) {
        Cache cache = cacheManager.getCache(cn.enilu.flash.bean.constant.cache.Cache.CONSTANT);
        cache.put(key,val);
    }

    @Override
    public <T>T get(Serializable key,Class<T> klass) {
        return cacheManager.getCache(cn.enilu.flash.bean.constant.cache.Cache.CONSTANT).get(String.valueOf(key),klass);
    }

    @Override
    public String get(Serializable key) {
        return cacheManager.getCache(cn.enilu.flash.bean.constant.cache.Cache.CONSTANT).get(String.valueOf(key),String.class);
    }

    @Override
    public void del(Serializable key) {
        cacheManager.getCache(cn.enilu.flash.bean.constant.cache.Cache.CONSTANT).evict(String.valueOf(key));
    }

    @Override
    public void hdel(Serializable key, Serializable k) {
        cacheManager.getCache(String.valueOf(key)).evict(String.valueOf(k));
    }
}
