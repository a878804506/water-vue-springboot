package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.constant.water.WaterConstant;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.water.WaterMeterRepository;

import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.WaterCommonUtile;
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
        //查询的表名
        String table = WaterConstant.WATER_METTER + year;
        String excu_page_sql = WaterCommonUtile.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_METTER_PAGE, table, name, (page.getCurrent() - 1) * page.getLimit(), page.getLimit());
        List<WaterMeter> records = waterMeterRepository.query(excu_page_sql);
        String excu_page_count_sql = WaterCommonUtile.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_METTER_PAGE_COUNT, table, name);
        page.setRecords(records);
        page.setTotal(Integer.valueOf(waterMeterRepository.getBySql(excu_page_count_sql) + ""));
        return page;
    }

    /**
     * 修改客户起止码信息
     *
     * @param waterWatermeter 起止码对象
     * @param year            年份
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateWatermeterById(WaterMeter waterWatermeter, String year) {
        //表名
        String table = WaterConstant.WATER_METTER + year;
        String updateWaterMeterSQL = WaterCommonUtile.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_METTER_UPDATE, waterWatermeterParamsToArray(table, waterWatermeter));
        waterMeterRepository.execute(updateWaterMeterSQL);
    }

    public String[] waterWatermeterParamsToArray(String table, WaterMeter tWaterWatermeter) {
        String result[] = new String[14];
        result[0] = table;
        result[1] = tWaterWatermeter.getOne() + "";
        result[2] = tWaterWatermeter.getTwo() + "";
        result[3] = tWaterWatermeter.getThree() + "";
        result[4] = tWaterWatermeter.getFour() + "";
        result[5] = tWaterWatermeter.getFive() + "";
        result[6] = tWaterWatermeter.getSix() + "";
        result[7] = tWaterWatermeter.getSeven() + "";
        result[8] = tWaterWatermeter.getEight() + "";
        result[9] = tWaterWatermeter.getNine() + "";
        result[10] = tWaterWatermeter.getTen() + "";
        result[11] = tWaterWatermeter.getEleven() + "";
        result[12] = tWaterWatermeter.getTwelve() + "";
        result[13] = tWaterWatermeter.getId() + "";
        return result;
    }
}

