package cn.enilu.flash.api.controller.water;

import cn.enilu.flash.bean.constant.water.WaterConstant;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.service.water.WaterMeterService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.factory.Page;

import cn.enilu.flash.warpper.water.WaterMeterWarpper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/water/watermeter")
public class WaterMeterController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaterMeterService waterMeterService;

    /**
     * 查询起止码列表
     *
     * @param name
     * @param year
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.CUSTOMER_WATER_METER})
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String year) {
        Page<WaterMeter> page = new PageFactory<WaterMeter>().defaultPage();
        if (StringUtils.isEmpty(year)) {
            // 没有年份就给默认的
            year = DateFormatUtils.format(new Date(), WaterConstant.YYYY);
        }
        if (null == name) {
            name = "";
        }
        name = WaterTemplateSQLConstant.PER_CENT + name + WaterTemplateSQLConstant.PER_CENT;
        page = waterMeterService.queryWaterMeterPage(page, name, year);
        // 字典字段转换
        List list = (List) new WaterMeterWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp();
        page.setRecords(list);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑客户起止码表", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.UPDATE_WATER_METER})
    public Object save(@ModelAttribute WaterMeter tWaterWatermeter) {
        if (tWaterWatermeter.getId() == null) {
            return Rets.failure("id不能为空!");
        } else {
            waterMeterService.updateWatermeterById(tWaterWatermeter, DateFormatUtils.format(new Date(), WaterConstant.YYYY));
        }
        return Rets.success();
    }
}