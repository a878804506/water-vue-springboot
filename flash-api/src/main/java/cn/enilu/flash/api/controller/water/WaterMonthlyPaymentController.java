package cn.enilu.flash.api.controller.water;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.service.water.WaterMonthlyPaymentService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.water.ExcelUtil;
import cn.enilu.flash.utils.water.NumToCNMoneyUtil;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 包月账单生成接口
 * 2022.5.22
 */
@RestController
@RequestMapping("/water/monthlyPayment")
public class WaterMonthlyPaymentController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WaterMonthlyPaymentController.class);

    @Value("${water.Excel.root.path}")
    private String waterExcelRootPath;

    @Value("${water.Excel.title}")
    private String waterExceltitle;

    private Map<String, WaterBill> hmap = new ConcurrentHashMap<>();

    @Autowired
    private WaterMonthlyPaymentService waterMonthlyPaymentService;
    @Autowired
    private TokenCache tokenCache;

    @RequestMapping(value = "/createWaterMonthlyPayment", method = RequestMethod.POST)
    @BussinessLog(value = "生成客户包月水费账单", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.CREATE_WATER_INFO})
    // months 月数
    public Object createWaterInfo(Integer months, Integer cid, Double price, boolean half, String times) {
        WaterBill waterBill = waterMonthlyPaymentService.createWaterInfo(months, cid, price, half, times);
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
            waterBill.setCharCapacityCost(WaterCommonUtil.DoubleToCharArray(8, waterBill.getCapacityCost()));
            waterBill.setCharWaterCost(WaterCommonUtil.DoubleToCharArray(8, waterBill.getWaterCost()));
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
            context.put("cname", waterBill.getCname());
            context.put("address", waterBill.getAddress());
            context.put("price", waterBill.getPrice());
            context.put("months", waterBill.getMonth());

            context.put("capacity.wan",waterBill.getCharCapacityCost()[0]);
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

            byte[] bytes = ExcelUtil.writeExcel(context, true);
            ExcelUtil.fileDownload(waterBill, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}