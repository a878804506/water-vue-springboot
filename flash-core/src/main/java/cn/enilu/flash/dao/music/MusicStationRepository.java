package cn.enilu.flash.dao.music;


import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;


public interface MusicStationRepository extends BaseRepository<MusicStation,Long>{
    MusicStation findById(String id);

    void deleteById(String id);

    List<MusicStation> findByIdIn(List<String> musicIds);
}

