package cn.enilu.flash.dao.music;


import cn.enilu.flash.bean.entity.music.MusicFavorite;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;


public interface MusicFavoriteRepository extends BaseRepository<MusicFavorite,Long>{

    List<MusicFavorite> findByFavoriteUserId(Long userId);

}

