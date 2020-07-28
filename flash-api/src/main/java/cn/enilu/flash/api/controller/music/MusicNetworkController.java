package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicNetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music/network")
public class MusicNetworkController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicNetworkService musicNetworkService;

    @RequestMapping(value = "/getMusicNetworkConfig", method = RequestMethod.GET)
    public Object getMusicNetworkConfig() {
        return Rets.success(musicNetworkService.getMusicNetworkConfig());
    }

}