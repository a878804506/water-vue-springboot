package cn.enilu.flash.service.music;


import cn.enilu.flash.bean.entity.music.MusicStation;
import cn.enilu.flash.dao.music.MusicStationRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class MusicStationService extends BaseService<MusicStation, Long, MusicStationRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MusicStationRepository musicStationRepository;

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
                if (null != platform) {
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
}

