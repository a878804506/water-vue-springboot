package cn.enilu.flash.service.system;


import cn.enilu.flash.bean.constant.cache.CacheKey;
import cn.enilu.flash.bean.entity.system.SysUrl;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.dao.system.SysUrlRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysUrlService extends BaseService<SysUrl,Long,SysUrlRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysUrlRepository sysUrlRepository;
    @Autowired
    private RedisCacheDao redisCacheDao;

    public List<SysUrl> getUrlSymbolList(){
        List redisUrlList = redisCacheDao.get(CacheKey.SYSTEM_URL_SYMBOL, List.class);
        if (null == redisUrlList) {
            List<SysUrl> all = sysUrlRepository.findByEnabled(1);
            redisCacheDao.set(CacheKey.SYSTEM_URL_SYMBOL, all);
            return all;
        } else {
            return redisUrlList;
        }
    }
}

