package cn.enilu.flash.dao.music;


import cn.enilu.flash.bean.entity.music.MusicFavoriteMapping;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;


public interface MusicFavoriteMappingRepository extends BaseRepository<MusicFavoriteMapping,Long>{

    List<MusicFavoriteMapping> findByFavoriteId(Long favoriteId);
}

