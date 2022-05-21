package cn.enilu.flash.api.controller.water;


import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.service.water.WaterInfoService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.ExcelUtil;
import cn.enilu.flash.utils.water.NumToCNMoneyUtil;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/water/info")
public class WaterInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WaterInfoController.class);

    @Value("${water.Excel.root.path}")
    private String waterExcelRootPath;

    @Value("${water.Excel.title}")
    private String waterExceltitle;

    private Map<String, WaterBill> hmap = new ConcurrentHashMap<>();

    @Autowired
    private WaterInfoService waterInfoService;
    @Autowired
    private TokenCache tokenCache;

    @RequestMapping(value = "/createWaterInfo", method = RequestMethod.POST)
    @BussinessLog(value = "生成客户月度水费账单", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CREATE_WATER_INFO})
    public Object createWaterInfo(Integer month, Integer cid, Double meterCode,@RequestParam(required = false) Double capacityCost, boolean half, String times) {
        if (month < 1 || month > 13) {
            return Rets.failure("月份非法!");
        }
        WaterBill waterBill = waterInfoService.createWaterInfo(month, cid, meterCode, capacityCost, half, times);
        hmap.put(cid + "-" + times, waterBill);
        return Rets.success(waterBill);
    }

    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @BussinessLog(value = "下载客户月度水费账单", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.DOWNLOAD_WATER_INFO})
    public void downloadExcel(@ModelAttribute WaterBill pre_waterBill, String token, String times) {
        try {
            WaterBill waterBill = hmap.get(pre_waterBill.getCid() + "-" + times);
            // double 转换成 char数组
            waterBill.setCharMeterageCost(waterInfoService.DoubleToCharArray(8, waterBill.getMeterageCost()));
            waterBill.setCharCapacityCost(waterInfoService.DoubleToCharArray(8, waterBill.getCapacityCost()));
            waterBill.setCharWaterCost(waterInfoService.DoubleToCharArray(8, waterBill.getWaterCost()));
            waterBill.setCapitalization(NumToCNMoneyUtil.number2CNMontrayUnit(waterBill.getWaterCost()));

            ShiroUser user = tokenCache.getUser(token);
            if (Objects.isNull(user)){
                return;
            }

            Map<String, Object> context = new HashMap<>();
            context.put("loginName", user.getName());
            context.put("ymd_hms", DateUtil.getTime());
            context.put("times", waterBill.getTimes());

            context.put("id", waterBill.getCid());
            context.put("title", waterExceltitle);
            context.put("year", waterBill.getYear());
            context.put("month", waterBill.getMonth());
            context.put("cname", waterBill.getCname());
            context.put("address", waterBill.getAddress());
            context.put("firstNumber", waterBill.getFirstNumber());
            context.put("lastNumber", waterBill.getLastNumber());
            context.put("waterCount", waterBill.getWaterCount());
            context.put("price", waterBill.getPrice());

            context.put("meterage.wan", waterBill.getCharMeterageCost()[0]);
            context.put("meterage.qian", waterBill.getCharMeterageCost()[1]);
            context.put("meterage.bai", waterBill.getCharMeterageCost()[2]);
            context.put("meterage.shi", waterBill.getCharMeterageCost()[3]);
            context.put("meterage.yuan", waterBill.getCharMeterageCost()[4]);
            context.put("meterage.jiao",waterBill.getCharMeterageCost()[6]);
            context.put("meterage.fen", waterBill.getCharMeterageCost()[7]);

            context.put("capacity.wan",waterBill.getCharMeterageCost()[0]);
            context.put("capacity.qian", waterBill.getCharCapacityCost()[1]);
            context.put("capacity.bai", waterBill.getCharCapacityCost()[2]);
            context.put("capacity.shi", waterBill.getCharCapacityCost()[3]);
            context.put("capacity.yuan", waterBill.getCharCapacityCost()[4]);
            context.put("capacity.jiao", waterBill.getCharCapacityCost()[6]);
            context.put("capacity.fen", waterBill.getCharCapacityCost()[7]);

            context.put("capitalization", waterBill.getCapitalization());
            context.put("cost.wan", waterBill.getCharWaterCost()[0]);
            context.put("cost.qian", waterBill.getCharWaterCost()[1]);
            context.put("cost.bai", waterBill.getCharWaterCost()[2]);
            context.put("cost.shi", waterBill.getCharWaterCost()[3]);
            context.put("cost.yuan", waterBill.getCharWaterCost()[4]);
            context.put("cost.jiao", waterBill.getCharWaterCost()[6]);
            context.put("cost.fen", waterBill.getCharWaterCost()[7]);

            byte[] bytes = ExcelUtil.writeExcel(context);
            ExcelUtil.fileDownload(waterBill, bytes);
        } catch (Exception e) {
            e.printStackTrace();
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