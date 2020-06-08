package cn.enilu.flash.service.music;

import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.dao.music.MusicSyncRepository;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MusicSyncTaskService
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 16:03
 **/
@Service
public class MusicSyncTaskService extends BaseService<MusicSync, Long, MusicSyncRepository> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MusicSyncRepository musicSyncRepository;

}
