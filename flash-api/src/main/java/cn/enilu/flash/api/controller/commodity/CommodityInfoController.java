package cn.enilu.flash.api.controller.commodity;

import cn.enilu.flash.bean.entity.commodity.CommodityInfo;
import cn.enilu.flash.service.commodity.CommodityInfoService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.factory.Page;

import cn.enilu.flash.warpper.commodity.CommodityInfoWarpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commodity/info")
public class CommodityInfoController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommodityInfoService commodityInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        Page<CommodityInfo> page = new PageFactory<CommodityInfo>().defaultPage();
        page = commodityInfoService.queryPage(page);
        // 字典字段转换
        page.setRecords((List) new CommodityInfoWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑商品信息表", key = "name", dict = CommonDict.class)
    public Object save(@ModelAttribute CommodityInfo tCommodityInfo) {
        if (tCommodityInfo.getId() == null) {
            commodityInfoService.insert(tCommodityInfo);
        } else {
            commodityInfoService.update(tCommodityInfo);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除商品信息表", key = "id", dict = CommonDict.class)
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        commodityInfoService.delete(id);
        return Rets.success();
    }
}