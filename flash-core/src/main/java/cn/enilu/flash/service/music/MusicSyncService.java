package cn.enilu.flash.service.music;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.constant.cache.CacheKey;
import cn.enilu.flash.bean.entity.music.MusicPlatform;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.bean.entity.system.Task;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.dao.music.MusicPlatformRepository;
import cn.enilu.flash.dao.music.MusicSyncRepository;
import cn.enilu.flash.service.task.TaskService;
import cn.enilu.flash.utils.HttpClientUtil;
import cn.enilu.flash.utils.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    private TaskService taskService;

    @Autowired
    private RedisCacheDao redisCacheDao;

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
        header.put("unlockCode", this.getUnlockCode());
        String searchList = HttpClientUtil.doGet(musicSearchUrltemp, header, null);
        JSONObject json = (JSONObject) JSON.parse(searchList);
        return json;
    }

    public String getUnlockCode() {
        // 先从缓存中查找
        String unlockCode = redisCacheDao.hget(Cache.MYKEY, CacheKey.MUSIC_UNLOCKCODE, String.class);
        if (StringUtils.isEmpty(unlockCode)) {
            // 查询音乐同步任务的信息，里面有unlockCode
            Task task = taskService.get(2l);
            JSONObject jsonObject = JSON.parseObject(task.getData());
            unlockCode = jsonObject.getString("unlockCode");
            redisCacheDao.hset(Cache.MYKEY, CacheKey.MUSIC_UNLOCKCODE, unlockCode);
        }
        return unlockCode;
    }

    public List<MusicPlatform> getPlatformsList() {
        // 先从缓存中查找
        List redisPlatformList = redisCacheDao.get(CacheKey.MUSIC_PLATFORM, List.class);
        if (null == redisPlatformList) {
            List<MusicPlatform> all = musicPlatformRepository.findAll();
            redisCacheDao.set(CacheKey.MUSIC_PLATFORM, all);
            return all;
        } else {
            return redisPlatformList;
        }
    }

    public String getPlatformsEnglishName(Integer platformId) {
        List<MusicPlatform> platformsList = this.getPlatformsList();
        for (MusicPlatform musicPlatform : platformsList) {
            if (musicPlatform.getId() == platformId) {
                return musicPlatform.getNameEn();
            }
        }
        return null;
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
