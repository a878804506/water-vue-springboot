package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.entity.music.MusicFavorite;
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

    public List<MusicFavorite> getFavoriteList() {
        Long userId = JwtUtil.getUserId();
        return musicFavoriteRepository.findByFavoriteUserId(userId);
    }

    @Override
    public MusicFavorite insert(MusicFavorite musicFavorite) {
        Long userId = JwtUtil.getUserId();
        musicFavorite.setFavoriteUserId(userId);
        return super.insert(musicFavorite);
    }

    public void updateMusicFavorite(MusicFavorite musicFavorite) {
        Long userId = JwtUtil.getUserId();
        musicFavorite.setFavoriteUserId(userId);
        super.update(musicFavorite);
    }

    @Override
    public void delete(Long id) {
        MusicFavorite musicFavorite = new MusicFavorite();
        Long userId = JwtUtil.getUserId();
        musicFavorite.setId(id);
        musicFavorite.setFavoriteUserId(userId);
        musicFavoriteRepository.delete(musicFavorite);
    }
}

