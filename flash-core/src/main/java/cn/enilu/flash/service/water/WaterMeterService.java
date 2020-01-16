package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.constant.water.WaterConstant;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.water.WaterMeterRepository;

import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaterMeterService extends BaseService<WaterMeter, Long, WaterMeterRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaterMeterRepository waterMeterRepository;

    /**
     * WaterMeter 列表查询
     *
     * @param page 分页组件
     * @param name 名字模糊查询
     * @param year 查询年份
     * @return
     */
    public Page<WaterMeter> queryWaterMeterPage(Page<WaterMeter> page, String name, String year) {
        if (StringUtils.isEmpty(year)) {
            /**
             * 查询本年
             */
            return this.queryPage(page);
        } else {
            /**
             * 查询往年表码值
             */
            //查询的表名
            String table = WaterConstant.WATER_METTER + year;
            String excu_page_sql = WaterCommonUtil.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_METTER_PAGE, table, name, (page.getCurrent() - 1) * page.getLimit(), page.getLimit());
            List<WaterMeter> records = waterMeterRepository.query(excu_page_sql);
            String excu_page_count_sql = WaterCommonUtil.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_METTER_PAGE_COUNT, table, name);
            page.setRecords(records);
            page.setTotal(Integer.valueOf(waterMeterRepository.getBySql(excu_page_count_sql) + ""));
            return page;
        }
    }

    /**
     * 修改客户起止码信息
     *
     * @param waterWatermeter 起止码对象
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateWatermeterById(WaterMeter waterWatermeter) {
        if(null == waterWatermeter.getThirteen()){
            waterWatermeter.setThirteen(waterWatermeter.getTwelve());
        }
        waterMeterRepository.save(waterWatermeter);
    }
}

