package cn.enilu.flash.api.controller.commodity;

import cn.enilu.flash.bean.entity.commodity.CommodityInventory;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.service.commodity.CommodityInventoryService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.factory.Page;

import cn.enilu.flash.warpper.commodity.CommodityInventoryWarpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commodity/inventory")
public class CommodityInventoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommodityInventoryService commodityInventoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        Page<CommodityInventory> page = new PageFactory<CommodityInventory>().defaultPage();
        page = commodityInventoryService.queryPage(page);
        // 字典字段转换
        page.setRecords((List) new CommodityInventoryWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
        return Rets.success(page);
    }

    @RequestMapping(value = "inOrOutInventory", method = RequestMethod.POST)
    @BussinessLog(value = "商品入库/出库", key = "name", dict = CommonDict.class)
    public Object inOrOutInventory(@ModelAttribute CommodityInventory commodityInventory, int type, String commodityRemarks) {
        if (type != 1 && type != 2) {
            return Rets.failure("类型出错");
        }
        Ret rets = commodityInventoryService.inOrOutInventory(commodityInventory, type, commodityRemarks);

        return rets;
    }
}