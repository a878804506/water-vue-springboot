package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.entity.music.MusicPlatform;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicSyncService;
import cn.enilu.flash.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 音乐同步操作接口
 * @ClassName MusicSyncController
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 17:28
 **/

@RestController
@RequestMapping("/musicSync")
public class MusicSyncController {

    @Autowired
    private MusicSyncService musicSyncService;

    /**
     * 站外音乐搜索
     * @param platform 平台
     * @param keyword  关键字
     * @param page 当前页
     * @param pageSize 一页多少条
     * @return
     */
    @RequestMapping(value = "/searchMusic", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MUSIC_SYNC})
    public Object searchMusic(@RequestParam() String platform,
                              @RequestParam() String keyword,
                              @RequestParam() Integer page,
                              @RequestParam() Integer pageSize) {

        JSONObject result = null;
        try {
            result = musicSyncService.searchMusic(platform,keyword,page,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return Rets.failure("请求错误");
        }
        if (500 == result.getBigInteger("code").intValue()) {
            return Rets.failure("请求出错，请重试");
        }
        return Rets.success(result.getJSONObject("data"));
    }

    /**
     *  获取全平台列表
     * @param needTran 是否进行转换，用于app上
     * @return
     */
    @RequestMapping(value = "/getPlatformsList", method = RequestMethod.GET)
    public Object getPlatformsList(@RequestParam(required = false)Boolean needTran) {
        List<MusicPlatform> platformsList = musicSyncService.getPlatformsList();
        if(null != needTran && needTran){
            List<Map<String,Object>> resultList = new ArrayList<>();
            Map<String,Object> all_temp = new HashMap<>();
            all_temp.put("title","全部");
            all_temp.put("value",0);
            resultList.add(all_temp);
            for(MusicPlatform musicPlatform :platformsList){
                Map<String,Object> temp = new HashMap<>();
                temp.put("title",musicPlatform.getNameCn());
                temp.put("value",musicPlatform.getId());
                resultList.add(temp);
            }
           return Rets.success(resultList);
        }
        return Rets.success(platformsList);
    }

    /**
     * 歌曲同步到站内
     * @param platform
     * @param keyword
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/syncSongs", method = RequestMethod.POST)
    @RequiresPermissions(value = {Permission.MUSIC_SYNC})
    public Object syncSongs(@RequestParam() String platform,
                            @RequestParam() String keyword,
                            @RequestParam() Integer page,
                            @RequestParam() Integer pageSize,
                            @RequestParam() String syncType,
                            HttpServletRequest request) {

        String musicSync = request.getParameter("musicSync");
        if(StringUtil.isEmpty(musicSync)){
            return Rets.failure("参数错误");
        }
        List<MusicSync> syncSongs = new ArrayList<>();
        try {
            syncSongs = JSON.parseArray(musicSync, MusicSync.class);
        }catch (Exception e){
            e.printStackTrace();
            return Rets.failure("JSON反序列化失败：参数错误");
        }
        List<MusicSync> musicSyncs = musicSyncService.syncSongs(syncType, platform, keyword, page, pageSize, syncSongs);
        if(musicSyncs.size() != 0){
            return Rets.success("添加的音乐中有部分音乐已重复添加，已经为您过滤");
        }
        return Rets.success("所选音乐已添加至待同步列表中");
    }
}
