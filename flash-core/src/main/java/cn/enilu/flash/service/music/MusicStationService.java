package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.dao.music.MusicStationRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class MusicStationService extends BaseService<MusicStation, Long, MusicStationRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicStationRepository musicStationRepository;
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

    public Page<MusicStation> queryPage(Page<MusicStation> page, Integer platform, String keyword) {
        Pageable pageable = null;
        pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), page.getSort());
        Specification<MusicStation> specification = new Specification<MusicStation>() {
            @Override
            public Predicate toPredicate(Root<MusicStation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                Predicate p = builder.conjunction();
                if (StringUtil.isNotEmpty(keyword)) {
                    // 模糊搜索
                    p = builder.and(p, builder.like(root.get("name"), "%" + keyword + "%"));
                    p = builder.or(p, builder.like(root.get("singers"), "%" + keyword + "%"));
                }
                if (null != platform && platform != 0) {
                    p = builder.and(p, builder.equal(root.get("platformId"), platform));
                }
                return p;
            }
        };
        org.springframework.data.domain.Page<MusicStation> pageResult = musicStationRepository.findAll(specification, pageable);
        page.setTotal(Integer.valueOf(pageResult.getTotalElements() + ""));
        page.setRecords(pageResult.getContent());
        return page;
    }

    public MusicStation getOne(String id) {
        return musicStationRepository.findById(id);
    }

    @Transactional
    public void deleteById(String id) {
        musicStationRepository.deleteById(id);
    }

    /**
     * 站内获取播放的url  使用了redis缓存
     * @param uuidFileName
     * @return
     */
    public String getMusicById(String uuidFileName) {
        String url = "";
        // redis 相关：
        if(redisMusicTemplate.hasKey(uuidFileName)){
            // 从缓存中取值
            url = redisMusicTemplate.opsForValue().get(uuidFileName);
        }else{
            OSS ossClient = null;
            try {
                ossClient = new OSSClientBuilder().build(aliyunSdkOss, aliyunSdkOssAccessKeyId, aliyunSdkOssAccessKeySecret);
                Date expiration = new Date(new Date().getTime()+musicTimeout);
                url = ossClient.generatePresignedUrl(aliyunMusicBucket, uuidFileName, expiration).toString();
                // 加入缓存
                redisMusicTemplate.opsForValue().set(uuidFileName,url,musicTimeout, TimeUnit.MILLISECONDS);
            }catch (Exception e){

            }finally {
                if(null != ossClient){
                    ossClient.shutdown();
                }
            }
        }
        return url;
    }

    /**
     * 删除OSS上的文件
     * @param uuidFileName
     * @return
     */
    public Boolean deleteOSSFile(String uuidFileName){
        OSS ossClient =null;
        try {
            ossClient = new OSSClientBuilder().build(aliyunSdkOss, aliyunSdkOssAccessKeyId, aliyunSdkOssAccessKeySecret);
            boolean hasFile = ossClient.doesObjectExist(aliyunMusicBucket, uuidFileName,true);
            if(!hasFile){
                return false;
            }
            ossClient.deleteObject(aliyunMusicBucket,uuidFileName);
        }catch (Exception e){
            return false;
        }finally {
            if(null != ossClient){
                ossClient.shutdown();
            }
        }
        return true;
    }
}

