package cn.enilu.flash.service.task.job;

import cn.enilu.flash.bean.entity.music.MusicPlatform;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.bean.entity.system.SysUrl;
import cn.enilu.flash.dao.music.MusicStationRepository;
import cn.enilu.flash.dao.music.MusicSyncRepository;
import cn.enilu.flash.service.music.MusicSyncService;
import cn.enilu.flash.service.system.SysUrlService;
import cn.enilu.flash.service.task.JobExecuter;
import cn.enilu.flash.utils.HttpClientUtil;
import cn.enilu.flash.utils.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @ClassName SyncMusicJob
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-05 15:59
 **/
@Component
public class SyncMusicJob extends JobExecuter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${music.search}")
    private String musicSearchUrl;

    @Value("${music.songInfo}")
    private String musicSongInfoUrl;

    @Value("${music.songPlayer}")
    private String musicSongPlayerUrl;

    @Value("${aliyun.sdk.oss.Endpoint}")
    private String aliyunSdkOss;

    @Value("${aliyun.sdk.oss.AccessKeyId}")
    private String aliyunSdkOssAccessKeyId;

    @Value("${aliyun.sdk.oss.AccessKeySecret}")
    private String aliyunSdkOssAccessKeySecret;

    @Value("${aliyun.musicBucket}")
    private String aliyunMusicBucket;

    @Autowired
    private MusicSyncRepository musicSyncRepository;

    @Autowired
    private MusicSyncService musicSyncService;

    @Autowired
    private MusicStationRepository musicStationRepository;

    @Autowired
    private SysUrlService sysUrlService;

    /**
     * 音乐平台列表
     */
    private Map<String, Integer> musicPlatformMap = new HashMap<>();

    /**
     * url转义列表
     */
    private List<SysUrl> urlSymbolList = new ArrayList<>();

    private OSS ossClient = null;

    @Override
    public String execute(Map<String, Object> dataMap) throws Exception {
        String result = "";
        List<MusicPlatform> musicPlatformList = musicSyncService.getPlatformsList();
        for (MusicPlatform temp : musicPlatformList) {
            musicPlatformMap.put(temp.getNameEn(), temp.getId());
        }
        urlSymbolList = sysUrlService.getUrlSymbolList();
        // 查找 0待同步，2同步失败的数据   备注：1同步成功 3没有相应品质的音乐资源  4指定条件下没有找到该歌曲
        List<MusicSync> notSuccessSongs = musicSyncRepository.findBySyncStatusIn(Arrays.asList(0, 2));

        if (notSuccessSongs.size() != 0) {
            ossClient = new OSSClientBuilder().build(aliyunSdkOss, aliyunSdkOssAccessKeyId, aliyunSdkOssAccessKeySecret);
            Map<String, String> header = new HashMap<>();
            header.put("unlockCode", musicSyncService.getUnlockCode());
            List<String> successSyncMusic = getPlayerUrls(notSuccessSongs, header);
            result = successSyncMusic.toString();
        } else {
            result = "SyncMusicJob : 暂无歌曲需要同步！";
        }
        if (ossClient != null) {
            ossClient.shutdown();
        }
        return result;
    }

    private List<String> getPlayerUrls(List<MusicSync> notSuccessSongs, Map<String, String> header) {
        List<String> result = new ArrayList<>();
        for (MusicSync musicSync : notSuccessSongs) {
            try {
                JSONObject jsonObject = musicSyncService.searchMusic(musicSync.getPlatform(), musicSync.getKeyword(), musicSync.getPage(), musicSync.getPageSize());
                if (!jsonObject.get("code").equals(200)) {
                    logger.error("获取歌曲列表失败!!");
                    musicSync.setSyncStatus(2);
                    continue;
                }
                List<MusicSync> allSongs = JSON.parseArray(jsonObject.getJSONObject("data").getJSONArray("list").toJSONString(), MusicSync.class);
                boolean hasSong = false;
                for (MusicSync all : allSongs) {
                    if (musicSync.equals(all)) {
                        musicSync.setId(all.getId());
                        hasSong = true;
                        break;
                    }
                }
                if (!hasSong) {
                    musicSync.setSyncStatus(4);
                    logger.error("指定关键字、页码没有找到该歌曲!!");
                    continue;
                }
                // 获取歌曲
                String song = HttpClientUtil.doGet(ToolUtil.replaceTemplate(musicSongInfoUrl, musicSync.getPlatform(), getTransformationUrl(musicSync.getId())), header, null);
                JSONObject songJSON = (JSONObject) JSON.parse(song);
                if (500 == songJSON.getBigInteger("code").intValue()) {
                    logger.error("根据id获取歌曲时出错" + musicSync.toString());
                    musicSync.setSyncStatus(2);
                    continue;
                }
                JSONObject realSong = songJSON.getJSONObject("data");
                if (!checkMusicHasSyncType(realSong, musicSync.getSyncType())) {
                    logger.debug("songInfo<该歌曲没有【" + musicSync.getSyncType() + "】品质的音质>--->" + realSong.toJSONString());
                    musicSync.setSyncStatus(3);
                    continue;
                }
                Thread.sleep(500);

                String songPlayerUrl = HttpClientUtil.doGet(ToolUtil.replaceTemplate(musicSongPlayerUrl, musicSync.getPlatform(),
                        getTransformationUrl(realSong.get("id").toString()), musicSync.getSyncType()), header, null);

                JSONObject songPlayerData = (JSONObject) JSON.parse(songPlayerUrl);
                if (500 == songPlayerData.getBigInteger("code").intValue()) {
                    logger.error("获取播放连接时出错--->" + musicSync.toString());
                    musicSync.setSyncStatus(2);
                    continue;
                }
                String playerUrl = songPlayerData.getJSONArray("data").get(0).toString();

                // 成功获取到下载链接后的操作
                MusicStation temp = new MusicStation(getTransformationUrl(musicSync.getId()), musicSync.getName(), musicSync.getSingers(), realSong.get("picUrl").toString(),
                        musicSync.getHasHQ(), musicSync.getHasSQ(), musicSync.getHasMV(), musicSync.getHasAlbum(), musicSync.getAlbumId(), musicSync.getAlbumName());

                String fileName = uploadFileToOSS(playerUrl, getMusicSuffix(musicSync.getSyncType()));
                musicSync.setSyncStatus(1);
                temp.setMusicUrl(fileName);

                switch (musicSync.getSyncType()) {
                    case "128":
                        temp.setMusicType(1);
                        break;
                    case "320":
                        temp.setMusicType(2);
                        break;
                    case "flac":
                        temp.setMusicType(3);
                        break;
                    default:
                        logger.error("没有匹配到歌曲的品质类型！");
                        break;
                }
                temp.setPlatformId(musicPlatformMap.get(musicSync.getPlatform()));
                musicStationRepository.saveAndFlush(temp);
                result.add(musicSync.getName() + "-" + musicSync.getPlatform() + "-" + musicSync.getSingers());
            } catch (Exception e) {
                logger.error("获取歌曲列表失败" + e.getMessage());
                musicSync.setSyncStatus(2);
            } finally {
                musicSyncRepository.saveAndFlush(musicSync);
            }
        }
        return result;
    }

    /**
     * 判断站外获取的歌曲 有没有目标品质的歌曲
     *
     * @param realSong
     * @param syncType
     * @return
     */
    private Boolean checkMusicHasSyncType(JSONObject realSong, String syncType) {
        Boolean result = false;
        switch (syncType) {
            case "128":
                result = true;
                break;
            case "320":
                result = realSong.getBoolean("hasHQ");
                break;
            case "flac":
                result = realSong.getBoolean("hasSQ");
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 获取音乐文件的后缀
     *
     * @param syncType
     * @return
     */
    private String getMusicSuffix(String syncType) {
        String result = "";
        switch (syncType) {
            case "128":
            case "320":
                result = ".mp3";
                break;
            case "flac":
                result = ".flac";
                break;
            default:
                break;
        }
        return result;
    }

    private String uploadFileToOSS(String playerUrl, String musicSuffix) throws Exception {
        String fileName = UUID.randomUUID().toString() + musicSuffix;
        InputStream inputStream = new URL(playerUrl).openStream();
        ossClient.putObject(aliyunMusicBucket, fileName, inputStream);
        return fileName;
    }

    /**
     * 对url中的id做转义
     *
     * @param id
     * @return
     */
    private String getTransformationUrl(String id) {
        for (SysUrl temp : urlSymbolList) {
            id = id.replace(temp.getSymbol(), temp.getTransformation());
        }
        return id;
    }
}
