package cn.enilu.flash.dao.music;


import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;
import java.util.Set;


public interface MusicStationRepository extends BaseRepository<MusicStation,Long>{
    MusicStation findById(String id);

    void deleteById(String id);

    List<MusicStation> findByIdIn(Set<String> musicIds);
}

