package cn.enilu.flash.service.music;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MusicNetworkService  {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${music.search}")
    private String musicSearchUrl;

    @Value("${music.songInfo}")
    private String musicSongInfoUrl;

    @Value("${music.songPlayer}")
    private String musicSongPlayerUrl;

    @Autowired
    private MusicSyncService musicSyncService;

    public Map<String,Object> getMusicNetworkConfig() {
        Map<String,Object> result = new HashMap<>();
        result.put("musicSearchUrl",musicSearchUrl);
        result.put("musicSongInfoUrl",musicSongInfoUrl);
        result.put("musicSongPlayerUrl",musicSongPlayerUrl);
        result.put("unlockCode",musicSyncService.getUnlockCode());
        return result;
    }
}

