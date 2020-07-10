package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.entity.music.MusicFavoriteMapping;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.vo.music.MusicStationVo;
import cn.enilu.flash.dao.music.MusicFavoriteMappingRepository;

import cn.enilu.flash.dao.music.MusicStationRepository;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusicFavoriteMappingService extends BaseService<MusicFavoriteMapping, Long, MusicFavoriteMappingRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteMappingRepository musicFavoriteMappingRepository;
    @Autowired
    private MusicStationRepository musicStationRepository;

    public boolean saveOrClose(MusicFavoriteMapping musicFavoriteMapping) {
        // 收藏 取消收藏逻辑没实现
        return false;
    }

    public List<MusicStationVo> getFavoriteMusicList(Long favoriteId) {
        // 查询收藏组ids
        List<MusicFavoriteMapping> byFavoriteId = musicFavoriteMappingRepository.findByFavoriteId(favoriteId);
        List<String> musicIds = new ArrayList<>();
        byFavoriteId.forEach(item -> musicIds.add(item.getMusicStationId()));
        // 根据收藏组ids  查询歌曲列表
        List<MusicStation> musicList = musicStationRepository.findByIdIn(musicIds);
        // 返回前端数据处理
        List<MusicStationVo> result = new ArrayList<>();
        musicList.forEach(item -> {
            MusicStationVo temp = new MusicStationVo(item.getId(),item.getName(),item.getSingers(),null,item.getPicUrl());
            result.add(temp);
        });
        return result;
    }
}

