package cn.enilu.flash.api.controller.water;


import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.water.WaterInfoService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.OperateExcelUtil;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/water/info")
public class WaterInfoController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${water.Excel.root.path}")
    private String waterExcelRootPath;

    @Autowired
    private WaterInfoService waterInfoService;

    @RequestMapping(value = "/createWaterInfo", method = RequestMethod.POST)
    @BussinessLog(value = "生成客户月度水费账单", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CREATE_WATER_INFO})
    public Object createWaterInfo(Integer month, Integer cid, Double meterCode) {
        if (month < 1 || month > 13) {
            return Rets.failure("月份非法!");
        }
        WaterBill waterBill = waterInfoService.createWaterInfo(month, cid, meterCode);
        return Rets.success(waterBill);
    }

    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @BussinessLog(value = "下载客户月度水费账单", key = "name", dict = CommonDict.class)
    public void downloadExcel(@ModelAttribute WaterBill waterBill) {
        if (StringUtils.isNotEmpty(waterBill.getExcelFileName())) {
            try {
                String yearMonth = System.getProperty("file.separator") + waterBill.getYear() + String.format("%02d", waterBill.getMonth());
                OperateExcelUtil.fileDownload(waterExcelRootPath, yearMonth, waterBill.getExcelFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/checkMeterCode", method = RequestMethod.GET)
    @BussinessLog(value = "判断本月止码是否有效", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CREATE_WATER_INFO})
    public Object checkMeterCode(Integer month, Integer cid, Double meterCode) {
        if (month < 1 || month > 13) {
            return Rets.failure("月份非法!");
        }
        Map result = waterInfoService.checkMeterCode(month, cid, meterCode);
        return Rets.success(result);
    }

    @RequestMapping(value = "/getWaterInfo", method = RequestMethod.GET)
    @BussinessLog(value = "页面初始化后和每次生成完账单后查询开票数量详情", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CUSTOMER_WATER_INFO})
    public Object getWaterInfo(Integer month) {
        if (month < 1 || month > 13) {
            return Rets.failure("月份非法!");
        }
        return Rets.success(waterInfoService.getWaterInfo(month));
    }

    @RequestMapping(value = "/getCustomersWaterCostByMonth", method = RequestMethod.GET)
    @BussinessLog(value = "全镇月度总水费详情", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CUSTOMERS_WATER_COST})
    public Object getCustomersWaterCostByMonth(Integer month) {
        if (month < 1 || month > 13) {
            return Rets.failure("月份非法!");
        }
        return Rets.success(waterInfoService.getCustomersWaterCostByMonth(month));
    }

    @RequestMapping(value = "/getToDayTabs", method = RequestMethod.GET)
    @BussinessLog(value = "查询今日录入水费详情的选项卡", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CUSTOMERS_WATER_COST_TO_DAY})
    public Object getToDayTabs() {
        return Rets.success(waterInfoService.getToDayTabs());
    }

    @RequestMapping(value = "/getToDayData", method = RequestMethod.GET)
    @BussinessLog(value = "查询今日录入水费详情", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CUSTOMERS_WATER_COST_TO_DAY})
    public Object getToDayData() {
        Page page = new PageFactory().defaultPage();
        page.addFilter(SearchFilter.build("modifyTime", SearchFilter.Operator.GT, WaterCommonUtil.getStartDate(new Date())));
        page.addFilter(SearchFilter.build("modifyTime", SearchFilter.Operator.LT, WaterCommonUtil.getEndDate(new Date())));
        // 手动排序 id正序
        page.setSort(new Sort(Sort.Direction.ASC,"modifyTime"));
        return Rets.success(waterInfoService.getToDayData(page));
    }
}