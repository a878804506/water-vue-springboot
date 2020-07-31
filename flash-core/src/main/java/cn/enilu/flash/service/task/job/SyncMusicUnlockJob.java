package cn.enilu.flash.service.task.job;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.constant.cache.CacheKey;
import cn.enilu.flash.bean.entity.system.Task;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.service.music.MusicSyncService;
import cn.enilu.flash.service.task.JobExecuter;
import cn.enilu.flash.service.task.TaskService;
import cn.enilu.flash.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName 爬取 https://wsmusic.sounm.com 网站的解锁码
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-30 10:37
 **/
@Component
public class SyncMusicUnlockJob extends JobExecuter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MusicSyncService musicSyncService;

    @Autowired
    private RedisCacheDao redisCacheDao;

    @Autowired
    private TaskService taskService;

    @Override
    public String execute(Map<String, Object> dataMap) throws Exception {
        String result = "";
        final String url = "https://wsmusic.sounm.com";
        final String checkUrl = "https://cdn.dnpw.org/api/qrcode";
        String unlockCode = null;
        try {
            //先获得的是整个页面的html标签页面
            Document doc = Jsoup.connect(url).get();
            Elements link = doc.head().getElementsByTag("link");
            for (Element element : link) {
                if (element.attr("href").startsWith("/js/app")) {
                    String js = element.attr("href");
                    String message = HttpClientUtil.doGet(url + js, null, null);
                    String[] split = message.split("\r\n");
                    for (String temp : split) {
                        if (temp.indexOf(checkUrl) != -1) {
                            unlockCode = getUnlockCode(temp.trim());
                            break;
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(unlockCode)) {
                if (unlockCode.equalsIgnoreCase(musicSyncService.getUnlockCode())) {
                    result = "SyncMusicUnlockJob : 获取到的解锁码与原解锁码一致！";
                } else {
                    redisCacheDao.hdel(Cache.MYKEY, CacheKey.MUSIC_UNLOCKCODE);
                    Task task = taskService.get(2l);
                    task.setData("{\"unlockCode\":\"" + unlockCode + "\"}");
                    taskService.update(task);
                    result = "成功更新解锁码！新的解锁码为：" + unlockCode;
                }
            } else {
                result = "SyncMusicUnlockJob : 没有获取到解锁码！";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUnlockCode(String url) {
        String unlockCode = null;
        try {
            String[] split = url.split(":");
            if (split.length == 4)
                unlockCode = split[3].substring(0, 4);
        } catch (Exception e) {
            logger.error(e.getMessage());
            unlockCode = null;
        }
        return unlockCode;
    }
}
