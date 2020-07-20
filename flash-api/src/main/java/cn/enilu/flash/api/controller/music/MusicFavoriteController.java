package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.music.MusicFavorite;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicFavoriteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/music/favorite")
public class MusicFavoriteController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicFavoriteService musicFavoriteService;

    @RequestMapping(value = "/getFavoriteList", method = RequestMethod.GET)
    public Object getFavoriteList() {
        return Rets.success(musicFavoriteService.getFavoriteList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "新增编辑音乐收藏", key = "name", dict = CommonDict.class)
    public Object save(@ModelAttribute MusicFavorite tMusicFavorite) {
        if(StringUtils.isEmpty(tMusicFavorite.getFavoriteName())){
            return Rets.failure("参数非法！");
        }
        if (tMusicFavorite.getId() == null) {
            List<MusicFavorite> favoriteList = musicFavoriteService.getFavoriteList();
            if(null != favoriteList && favoriteList.size() >= 10)
                return Rets.failure("个人收藏分组最多只能有10个！");
            musicFavoriteService.insert(tMusicFavorite);
        } else {
            musicFavoriteService.updateMusicFavorite(tMusicFavorite);
        }
        return Rets.success(musicFavoriteService.getFavoriteList());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除音乐收藏", key = "id", dict = CommonDict.class)
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        musicFavoriteService.delete(id);
        return Rets.success(musicFavoriteService.getFavoriteList());
    }
}