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

import java.util.ArrayList;
import java.util.List;

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
            // 收藏 取消收藏逻辑没实现
            if (isFavorite) {
                // 新增收藏
                if (musicFavoriteMapping.getFavoriteId() != null && StringUtils.isNotEmpty(musicFavoriteMapping.getMusicStationId())) {
                    MusicFavoriteMapping save = musicFavoriteMappingRepository.save(musicFavoriteMapping);
                    List<MusicFavoriteMapping> favoriteMappings = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + musicFavoriteMapping.getFavoriteId(), List.class);
                    if (null == favoriteMappings) {
                        favoriteMappings = new ArrayList<>();
                    }
                    favoriteMappings.add(save);
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
        List<MusicFavoriteMapping> favoriteMappings = this.getFavoriteMusicMappings(favoriteId);
        List<String> musicIds = new ArrayList<>();
        favoriteMappings.forEach(item -> musicIds.add(item.getMusicStationId()));
        // 根据收藏组ids  查询歌曲列表
        List<MusicStation> musicList = musicStationRepository.findByIdIn(musicIds);
        // 返回前端数据处理
        List<MusicStationVo> result = new ArrayList<>();
        musicList.forEach(item -> {
            MusicStationVo temp = new MusicStationVo(item.getId(), item.getName(), item.getSingers(), null, item.getPicUrl());
            result.add(temp);
        });
        return result;
    }

    public List<MusicFavoriteMapping> getFavoriteMusicMappings(Long favoriteId) {
        List<MusicFavoriteMapping> favoriteMappings;
        Long userId = JwtUtil.getUserId();
        favoriteMappings = redisCacheDao.hget(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, List.class);
        if (null == favoriteMappings || favoriteMappings.size() == 0) {
            // 查询收藏组ids
            favoriteMappings = musicFavoriteMappingRepository.findByFavoriteId(favoriteId);
            redisCacheDao.hset(Cache.MYKEY, userId + "_FavoriteMusicMappings_" + favoriteId, favoriteMappings);
        }
        return favoriteMappings;
    }
}

