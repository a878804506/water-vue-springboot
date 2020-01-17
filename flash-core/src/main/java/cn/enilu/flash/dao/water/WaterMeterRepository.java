package cn.enilu.flash.dao.water;

import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.BaseRepository;

import java.util.Collection;
import java.util.List;

public interface WaterMeterRepository extends BaseRepository<WaterMeter, Long> {

    WaterMeter findByCid(Integer cid);

    List<WaterMeter> findByCidNotIn(Collection<Integer> cid);
}

