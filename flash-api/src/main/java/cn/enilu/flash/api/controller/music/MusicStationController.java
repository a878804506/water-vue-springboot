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
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${aliyun.sdk.oss.Endpoint}")
    private String aliyunSdkOss;

    @Value("${aliyun.sdk.oss.AccessKeyId}")
    private String aliyunSdkOssAccessKeyId;

    @Value("${aliyun.sdk.oss.AccessKeySecret}")
    private String aliyunSdkOssAccessKeySecret;

    @Value("${aliyun.musicBucket}")
    private String aliyunMusicBucket;

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
            OSS ossClient = new OSSClientBuilder().build(aliyunSdkOss, aliyunSdkOssAccessKeyId, aliyunSdkOssAccessKeySecret);
            boolean hasFile = ossClient.doesObjectExist(aliyunMusicBucket, musicStation.getMusicUrl(),true);
            if(!hasFile){
                return Rets.failure("OSS上没有该文件");
            }
            ossClient.deleteObject(aliyunMusicBucket,musicStation.getMusicUrl());
            musicStationService.deleteById(id);
            return Rets.success();
        }else{
            return Rets.failure("删除失败");
        }
    }
}