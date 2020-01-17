package cn.enilu.flash.dao.water;

import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.dao.BaseRepository;

import java.util.Date;
import java.util.List;

public interface WaterInfoRepository extends BaseRepository<WaterInfo, Long> {


    int countByYearAndMonth(int year,int month);

    int countByModifyTimeBetween(Date startTime, Date endTime);

    List<WaterInfo> findByYearAndMonth(int year,int month);
}

