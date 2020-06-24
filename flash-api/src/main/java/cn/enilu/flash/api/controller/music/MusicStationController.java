package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicStationService;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music/station")
public class MusicStationController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicStationService musicStationService;




    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) Integer platform,
                       @RequestParam(required = false) String keyword) {
        Page<MusicStation> page = new PageFactory<MusicStation>().defaultPage();
        page.setSort(new Sort(Sort.Direction.DESC,"createTime"));
        page = musicStationService.queryPage(page,platform,keyword);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除站内音乐", key = "id", dict = CommonDict.class)
    public Object remove(String id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        MusicStation musicStation = musicStationService.getOne(id);
        if(null != musicStation){
            // 删除oss上的文件
            musicStationService.deleteOSSFile(musicStation.getMusicUrl());
            // 删除记录
            musicStationService.deleteById(id);
            return Rets.success();
        }else{
            return Rets.failure("删除失败");
        }
    }

    /**
     * 站内获取音乐url
     * @param id
     * @return
     */
    @RequestMapping(value = "/getMusicById", method = RequestMethod.GET)
    public Object getMusicById(@RequestParam String id) {
        MusicStation musicStation = musicStationService.getOne(id);
        if(null == musicStation){
            return Rets.failure("歌曲不存在");
        }
        return Rets.success(musicStationService.getMusicById(musicStation.getMusicUrl()));
    }
}