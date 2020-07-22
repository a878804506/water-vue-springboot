package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.music.MusicFavoriteMapping;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicFavoriteMappingService;
import cn.enilu.flash.service.music.MusicStationService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/music/favorite/mapping")
public class MusicFavoriteMappingController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteMappingService musicFavoriteMappingService;
    @Autowired
    private MusicStationService musicStationService;

    @RequestMapping(value = "/getFavoriteMusicList", method = RequestMethod.GET)
    public Object getFavoriteMusicList(Long favoriteId) {
        if (favoriteId == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        return Rets.success(musicFavoriteMappingService.getFavoriteMusicList(favoriteId));
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "新增 取消音乐收藏", key = "name", dict = CommonDict.class)
    public Object saveOrClose(@ModelAttribute MusicFavoriteMapping musicFavoriteMapping,
                              Boolean isFavorite,
                              @RequestParam(required = false) Integer platform,
                              @RequestParam(required = false) String keyword) {
        if (musicFavoriteMapping.getMusicStationId() == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        boolean result = musicFavoriteMappingService.saveOrClose(musicFavoriteMapping, isFavorite);
        if (!result)
            return Rets.failure("操作失败");
        Page<MusicStation> page = new PageFactory<MusicStation>().defaultPage();
        page.setSort(new Sort(Sort.Direction.DESC, "createTime"));
        Page<MusicStation> musicStationPage = musicStationService.queryPage(page, platform, keyword);
        return Rets.success(musicStationPage);
    }

    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    @BussinessLog(value = "批量收藏")
    public Object addList(Long favoriteId,
                          @RequestParam(required = false) Integer platform,
                          @RequestParam(required = false) String keyword,
                          HttpServletRequest request) {
        String temp = request.getParameter("musicStationIds");
        if(StringUtil.isEmpty(temp)){
            return Rets.failure("参数错误");
        }
        List<String> musicStationIds;
        try {
            musicStationIds = JSON.parseArray(temp, String.class);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Rets.failure("JSON反序列化失败：参数错误");
        }
        if (favoriteId == null ) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        musicFavoriteMappingService.addList(favoriteId, musicStationIds);

        Page<MusicStation> page = new PageFactory<MusicStation>().defaultPage();
        page.setSort(new Sort(Sort.Direction.DESC, "createTime"));
        Page<MusicStation> musicStationPage = musicStationService.queryPage(page, platform, keyword);
        return Rets.success(musicStationPage);
    }
}