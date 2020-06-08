package cn.enilu.flash.api.controller.music;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MusicStationController
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 17:28
 **/

@RestController
@RequestMapping("/musicStation")
public class MusicStationController {

//    @Autowired
//    private MusicSyncService musicSyncService;
//
//    @RequestMapping(value = "/searchMusic", method = RequestMethod.GET)
//    @RequiresPermissions(value = {Permission.MUSIC_SYNC})
//    public Object searchMusic(@RequestParam() String platform,
//                              @RequestParam() String keyword,
//                              @RequestParam() Integer page,
//                              @RequestParam() Integer pageSize) {
//
//        musicSyncService.syncMusic("tencent","周杰伦",0,20);
//
//        return Rets.success(null);
//    }
}
