package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicStationService;
import cn.enilu.flash.service.music.MusicSyncService;
import cn.enilu.flash.utils.factory.Page;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/music/app")
public class MusicAppApiController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicStationService musicStationService;
    @Autowired
    private MusicSyncService musicSyncService;

    /**
     * @param searchType 搜索类型 0：站内搜索，1：站外搜索
     * @param platformId 指定搜索的音乐平台id
     * @param keyword    搜索关键字
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam Integer searchType,
                       @RequestParam(required = false) Integer platformId,
                       @RequestParam(required = false) String keyword,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        if (0 == searchType) {
            Page<MusicStation> pageDatas = new PageFactory<MusicStation>().defaultPage();
            pageDatas.setSort(new Sort(Sort.Direction.DESC, "createTime"));
            pageDatas = musicStationService.queryPage(pageDatas, platformId, keyword);
            pageDatas.getRecords().forEach(music -> music.setSearchType(searchType));
            return Rets.success(pageDatas);
        } else if (1 == searchType) {
            Page<MusicStation> pageDatas = new PageFactory<MusicStation>().defaultPage();
            try {
                JSONObject jsonObject = musicSyncService.searchMusic(musicSyncService.getPlatformsEnglishName(platformId), keyword, page, limit);
                if (jsonObject.getIntValue("code") != 200) {
                    return Rets.failure("站外请求错误");
                }
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                List<MusicStation> records = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    MusicStation m = new MusicStation();
                    m.setPicUrl(temp.getString("picUrl"));
                    m.setName(temp.getString("name"));
                    m.setSingers(temp.getString("singers"));
                    m.setAlbumName(temp.getString("albumName"));
                    m.setSearchType(searchType);
                    m.setId(temp.getString("id"));
                    m.setPlatformId(platformId);
                    records.add(m);
                }
                pageDatas.setTotal(jsonObject.getJSONObject("data").getIntValue("total"));
                pageDatas.setRecords(records);
                return Rets.success(pageDatas);
            } catch (Exception e) {
                e.printStackTrace();
                return Rets.failure("站外请求错误");
            }
        } else {
            return Rets.failure("参数非法");
        }
    }

    @RequestMapping(value = "/getAppMusicUrl", method = RequestMethod.GET)
    public Object list(@RequestParam Integer searchType,
                       @RequestParam String id,
                       @RequestParam(required = false) Integer platformId) {
        if (0 == searchType) {
            MusicStation musicStation = musicStationService.getOne(id);
            if(null == musicStation)
                return Rets.failure("没有获取到播放资源");
            return Rets.success(musicStationService.getMusicById(musicStation.getMusicUrl()));
        } else if (1 == searchType) {
            return Rets.failure("站外参数非法");
        }else {
            return Rets.failure("参数非法");
        }
    }
}