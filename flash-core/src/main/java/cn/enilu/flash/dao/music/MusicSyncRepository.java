package cn.enilu.flash.dao.music;


import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MusicSyncRepository extends BaseRepository<MusicSync, Long> {

    List<MusicSync> findBySyncStatusIn(List<Integer> a);

    @Query(value = "insert into t_music_sync(platform,name,singers,has_hq,has_sq,has_mv,has_album," +
            "album_id,album_name,sync_type,sync_status,keyword,page,page_size,create_by,create_time" +
            ") values (:#{#musicSync.platform},:#{#musicSync.name},:#{#musicSync.singers},:#{#musicSync.hasHQ},:#{#musicSync.hasSQ},:#{#musicSync.hasMV}," +
            ":#{#musicSync.hasAlbum},:#{#musicSync.albumId},:#{#musicSync.albumName},:#{#musicSync.syncType}," +
            ":#{#musicSync.syncStatus},:#{#musicSync.keyword},:#{#musicSync.page},:#{#musicSync.pageSize}," +
            ":#{#musicSync.createBy},:#{#musicSync.createTime})", nativeQuery = true)
    @Modifying
    @Transactional
    int insertSyncSong(@Param("musicSync") MusicSync musicSync);
}

