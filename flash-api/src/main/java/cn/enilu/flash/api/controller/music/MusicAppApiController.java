package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.music.MusicStationService;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music/app")
public class MusicAppApiController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicStationService musicStationService;
    @Autowired
    private StringRedisTemplate redisMusicTemplate;

    @Value("${aliyun.sdk.oss.Endpoint}")
    private String aliyunSdkOss;

    @Value("${aliyun.sdk.oss.AccessKeyId}")
    private String aliyunSdkOssAccessKeyId;

    @Value("${aliyun.sdk.oss.AccessKeySecret}")
    private String aliyunSdkOssAccessKeySecret;

    @Value("${aliyun.musicBucket}")
    private String aliyunMusicBucket;

    @Value("${spring.redis.music.timeout}")
    private Long musicTimeout;

    /**
     *
     * @param searchType 搜索类型 0：站内搜索，1：站外搜索
     * @param platform  指定搜索的音乐平台
     * @param keyword  搜索关键字
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam Integer searchType,
                       @RequestParam(required = false) Integer platform,
                       @RequestParam(required = false) String keyword,
                       @RequestParam Integer page,
                       @RequestParam Integer limit) {
        if(0 == searchType){
            Page<MusicStation> pageDatas = new PageFactory<MusicStation>().defaultPage();
            pageDatas.setSort(new Sort(Sort.Direction.DESC,"createTime"));
            pageDatas = musicStationService.queryPage(pageDatas,platform,keyword);
            return Rets.success(pageDatas);
        }else if(1 == searchType){


            return Rets.success();
        }else{
            return Rets.failure("参数非法");
        }
    }

}