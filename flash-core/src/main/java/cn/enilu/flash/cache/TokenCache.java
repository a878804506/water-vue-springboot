package cn.enilu.flash.cache;

import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登录时，生成的Token与用户ID的对应关系
 */
@Service
public class TokenCache {

    @Autowired
    private RedisCacheDao redisCacheDao;

    public void put(String token, Long idUser) {
        redisCacheDao.hset(RedisCacheDao.SESSION, token, idUser);
    }

    public Long get(String token) {
        return redisCacheDao.hget(RedisCacheDao.SESSION, token, Long.class);
    }

    public Long getIdUser() {
        return redisCacheDao.hget(RedisCacheDao.SESSION, HttpUtil.getToken(), Long.class);
    }

    public void remove(String token) {
        redisCacheDao.hdel(RedisCacheDao.SESSION, token + "user");
    }

    public void setUser(String token, ShiroUser shiroUser) {
        redisCacheDao.hset(RedisCacheDao.SESSION, token + "user", shiroUser);
    }

    public ShiroUser getUser(String token) {
        return redisCacheDao.hget(RedisCacheDao.SESSION, token + "user", ShiroUser.class);
    }
}
