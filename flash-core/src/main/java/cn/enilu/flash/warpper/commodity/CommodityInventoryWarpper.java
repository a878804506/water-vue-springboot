package cn.enilu.flash.warpper.commodity;

import cn.enilu.flash.bean.entity.commodity.CommodityInfo;
import cn.enilu.flash.service.system.impl.ConstantFactory;
import cn.enilu.flash.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CommodityWarpper
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-05-13 12:14
 **/
public class CommodityInventoryWarpper extends BaseControllerWarpper {

    public CommodityInventoryWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("commodityTypeName", ConstantFactory.me().getCommodityInfoType(((CommodityInfo)map.get("commodityInfo")).getCommodityType()));
    }
}
