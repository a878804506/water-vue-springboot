package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.entity.music.MusicFavorite;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.dao.music.MusicFavoriteRepository;

import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicFavoriteService extends BaseService<MusicFavorite, Long, MusicFavoriteRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteRepository musicFavoriteRepository;
    @Autowired
    private RedisCacheDao redisCacheDao;

    public List<MusicFavorite> getFavoriteList() {
        Long userId = JwtUtil.getUserId();
        return musicFavoriteRepository.findByFavoriteUserId(userId);
    }

    public MusicFavorite getFavoriteByIdAndUserId(Long favoriteId) {
        Long userId = JwtUtil.getUserId();
        MusicFavorite result = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusic_" + favoriteId, MusicFavorite.class);
        if(null == result){
            result = musicFavoriteRepository.findByIdAndFavoriteUserId(favoriteId, userId);
            redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusic_" + favoriteId, result);
        }
        return result;
    }

    @Override
    public MusicFavorite insert(MusicFavorite musicFavorite) {
        Long userId = JwtUtil.getUserId();
        musicFavorite.setFavoriteUserId(userId);
        musicFavorite = super.insert(musicFavorite);
        redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusic_" + musicFavorite.getId(), musicFavorite);
        return musicFavorite;
    }

    public void updateMusicFavorite(MusicFavorite musicFavorite) {
        Long userId = JwtUtil.getUserId();
        musicFavorite.setFavoriteUserId(userId);
        super.update(musicFavorite);
        redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusic_" + musicFavorite.getId(), musicFavorite);
    }

    @Override
    public void delete(Long id) {
        MusicFavorite musicFavorite = new MusicFavorite();
        Long userId = JwtUtil.getUserId();
        musicFavorite.setId(id);
        musicFavorite.setFavoriteUserId(userId);
        musicFavoriteRepository.delete(musicFavorite);
        redisCacheDao.hdel(Cache.MYKEY, userId + "_FavoriteMusic_" + musicFavorite.getId());
    }
}

