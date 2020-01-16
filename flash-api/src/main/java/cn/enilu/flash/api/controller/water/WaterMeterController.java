package cn.enilu.flash.api.controller.water;

import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.water.WaterMeterService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
     * @param cname
     * @param year
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.CUSTOMER_WATER_METER})
    public Object list(@RequestParam(required = false) String cname,
                       @RequestParam(required = false) String year) {
        Page<WaterMeter> page = new PageFactory<WaterMeter>().defaultPage();
        if (StringUtil.isNotEmpty(cname)) {
            // 模糊搜索
            page.addFilter(SearchFilter.build("cname", SearchFilter.Operator.LIKE, cname));
        }else{
            cname = "";
        }
        // 手动排序 id正序
        page.setSort(new Sort(Sort.Direction.ASC,"id"));
        cname = WaterTemplateSQLConstant.PER_CENT + cname + WaterTemplateSQLConstant.PER_CENT;
        page = waterMeterService.queryWaterMeterPage(page, cname, year);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑客户起止码表", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.UPDATE_WATER_METER})
    public Object save(@ModelAttribute WaterMeter tWaterWatermeter) {
        if (tWaterWatermeter.getId() == null) {
            return Rets.failure("id不能为空!");
        } else {
            waterMeterService.updateWatermeterById(tWaterWatermeter);
        }
        return Rets.success();
    }
}