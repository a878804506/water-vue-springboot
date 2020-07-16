package cn.enilu.flash.api.config;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.security.JwtUtil;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 使用redis 作缓存
 * @ClassName RedisCacheConfig
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-05-11 12:24
 **/
@Configuration
public class RedisCacheConfig {

    @Bean
    CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        //common信息缓存配置
        RedisCacheConfiguration userCacheConfiguration = defaultCacheConfig
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())).disableCachingNullValues();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //entryTtl设置缓存失效时间，单位是秒
        //设置 登陆用户的token 在redis里面的过期时间  EhcacheDao.SESSION
        redisCacheConfigurationMap.put(Cache.SESSION, userCacheConfiguration.entryTtl(Duration.ofMillis(Cache.SESSION_EXPIRE_TIME)));

        //设置 自定义缓存组 的过期时间
        redisCacheConfigurationMap.put(Cache.MYKEY, userCacheConfiguration.entryTtl(Duration.ofMillis(Cache.MYKEY_EXPIRE_TIME)));

        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("common");
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(userCacheConfiguration)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
        return cacheManager;
    }
}
