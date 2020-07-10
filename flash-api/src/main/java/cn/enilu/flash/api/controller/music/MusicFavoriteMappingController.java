package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.music.MusicFavoriteMapping;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicFavoriteMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music/favorite/mapping")
public class MusicFavoriteMappingController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteMappingService musicFavoriteMappingService;

    @RequestMapping(value = "/getFavoriteMusicList", method = RequestMethod.GET)
    public Object getFavoriteMusicList(Long favoriteId) {
        if (favoriteId == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        return Rets.success(musicFavoriteMappingService.getFavoriteMusicList(favoriteId));
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "新增 取消音乐收藏", key = "name", dict = CommonDict.class)
    public Object saveOrClose(@ModelAttribute MusicFavoriteMapping musicFavoriteMapping) {
        if (musicFavoriteMapping.getMusicStationId() == null ||
                musicFavoriteMapping.getFavoriteId() == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        musicFavoriteMappingService.saveOrClose(musicFavoriteMapping);
        return Rets.success();
    }

}