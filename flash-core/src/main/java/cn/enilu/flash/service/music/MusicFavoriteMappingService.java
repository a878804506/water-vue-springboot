package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.entity.music.MusicFavorite;
import cn.enilu.flash.bean.entity.music.MusicFavoriteMapping;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.vo.music.MusicStationVo;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.dao.music.MusicFavoriteMappingRepository;

import cn.enilu.flash.dao.music.MusicStationRepository;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MusicFavoriteMappingService extends BaseService<MusicFavoriteMapping, Long, MusicFavoriteMappingRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteMappingRepository musicFavoriteMappingRepository;
    @Autowired
    private MusicStationRepository musicStationRepository;
    @Autowired
    private MusicFavoriteService musicFavoriteService;
    @Autowired
    private RedisCacheDao redisCacheDao;

    @Transactional
    public boolean saveOrClose(MusicFavoriteMapping musicFavoriteMapping, Boolean isFavorite) {
        try {
            Long userId = JwtUtil.getUserId();
            if (isFavorite) {
                // 新增收藏
                if (musicFavoriteMapping.getFavoriteId() != null && StringUtils.isNotEmpty(musicFavoriteMapping.getMusicStationId())) {
                    MusicFavoriteMapping save = musicFavoriteMappingRepository.save(musicFavoriteMapping);
                    LinkedHashSet<String> favoriteMappings = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + musicFavoriteMapping.getFavoriteId(), LinkedHashSet.class);
                    if (null == favoriteMappings) {
                        favoriteMappings = new LinkedHashSet<>();
                    }
                    favoriteMappings.add(save.getMusicStationId());
                    redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + musicFavoriteMapping.getFavoriteId(), favoriteMappings);
                }
            } else {
                // 删除收藏  这里是根据歌曲id进行删除，所有收藏组 收藏了该歌曲 都会被删除！
                List<MusicFavorite> favoriteList = musicFavoriteService.getFavoriteList();
                favoriteList.forEach(favorite -> {
                    musicFavoriteMapping.setFavoriteId(favorite.getId());
                    musicFavoriteMappingRepository.delete(musicFavoriteMapping);
                    redisCacheDao.hdel(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favorite.getId());
                });
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public List<MusicStationVo> getFavoriteMusicList(Long favoriteId) {
        LinkedHashSet<String> favoriteMappings = this.getFavoriteMusicMappings(favoriteId);
        // 根据收藏组ids  查询歌曲列表
        List<MusicStation> musicList = musicStationRepository.findByIdIn(favoriteMappings);
        // 返回前端数据处理
        List<MusicStationVo> result = new ArrayList<>();
        musicList.forEach(item -> {
            MusicStationVo temp = new MusicStationVo(item.getId(), item.getName(), item.getSingers(), null, item.getPicUrl());
            result.add(temp);
        });
        return result;
    }

    public LinkedHashSet<String> getFavoriteMusicMappings(Long favoriteId) {
        LinkedHashSet<String> favoriteMappings;
        Long userId = JwtUtil.getUserId();
        favoriteMappings = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, LinkedHashSet.class);
        if (null == favoriteMappings || favoriteMappings.size() == 0) {
            favoriteMappings = new LinkedHashSet<>();
            // 查询收藏组ids
            List<MusicFavoriteMapping> byFavoriteId = musicFavoriteMappingRepository.findByFavoriteId(favoriteId);
            for(MusicFavoriteMapping temp :byFavoriteId){
                favoriteMappings.add(temp.getMusicStationId());
            }
            redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, favoriteMappings);
        }
        return favoriteMappings;
    }

    public void addList(Long favoriteId, List<String> musicStationIds) {
        Set<MusicFavoriteMapping> ids = new LinkedHashSet<>();
        Set<String> redisIds = new LinkedHashSet<>();
        musicStationIds.forEach(musicStationId -> {
            MusicFavoriteMapping temp = new MusicFavoriteMapping();
            temp.setFavoriteId(favoriteId);
            temp.setMusicStationId(musicStationId);
            redisIds.add(musicStationId);
            ids.add(temp);
        });
        if(ids.size() != 0) {
            Long userId = JwtUtil.getUserId();
            // 收藏持久化
            musicFavoriteMappingRepository.saveAll(ids);
            LinkedHashSet<String> favoriteMappings = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, LinkedHashSet.class);
            if(null != favoriteMappings)
                redisIds.addAll(favoriteMappings);
            redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, redisIds);
        }
    }
}

