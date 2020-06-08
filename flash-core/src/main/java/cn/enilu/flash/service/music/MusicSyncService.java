package cn.enilu.flash.service.music;

import cn.enilu.flash.bean.entity.music.MusicPlatform;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.dao.music.MusicPlatformRepository;
import cn.enilu.flash.dao.music.MusicSyncRepository;
import cn.enilu.flash.utils.HttpClientUtil;
import cn.enilu.flash.utils.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName MusicSyncService
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 16:03
 **/
@Service
public class MusicSyncService {

    @Value("${music.search}")
    private String musicSearchUrl;

    @Autowired
    private MusicPlatformRepository musicPlatformRepository;

    @Autowired
    private MusicSyncRepository musicSyncRepository;

    /**
     * 站外音乐搜索
     *
     * @param platform 搜索平台
     * @param keyword  搜索关键字
     * @param page     当前页
     * @param pageSize 一页条数
     * @return
     */
    public JSONObject searchMusic(String platform, String keyword, Integer page, Integer pageSize) throws Exception {
        String musicSearchUrltemp = ToolUtil.replaceTemplate(musicSearchUrl, platform, keyword, page, pageSize);
        Map<String, String> header = new HashMap<>();
        header.put("unlockCode", "8943");
        String searchList = HttpClientUtil.doGet(musicSearchUrltemp, header, null);
        JSONObject json = (JSONObject) JSON.parse(searchList);
        return json;
    }

    public List<MusicPlatform> getPlatformsList() {
        return musicPlatformRepository.findAll();
    }

    /**
     * 异步线程同步，同步结果写入日志
     *
     * @param platform
     * @param keyword
     * @param page
     * @param pageSize
     * @param syncSongs
     */
    public List<MusicSync> syncSongs(String syncType, String platform, String keyword, Integer page, Integer pageSize, List<MusicSync> syncSongs) {
        List<MusicSync> notSyncSongs = new ArrayList();
        Long loginUserId = ToolUtil.getCurrentAuditor();
        Date thisDate = new Date();

        for (MusicSync musicSync : syncSongs) {
            musicSync.setPage(page);
            musicSync.setPageSize(pageSize);
            musicSync.setKeyword(keyword);
            musicSync.setPlatform(platform);
            musicSync.setSyncType(syncType);
            musicSync.setSyncStatus(0);
            musicSync.setCreateBy(loginUserId);
            musicSync.setCreateTime(thisDate);
            try {
                musicSyncRepository.insertSyncSong(musicSync);
            } catch (Exception e) {
                notSyncSongs.add(musicSync);
            }
        }
        return notSyncSongs;
    }
}
