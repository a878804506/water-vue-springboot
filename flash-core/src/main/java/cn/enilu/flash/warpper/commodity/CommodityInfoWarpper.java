package cn.enilu.flash.warpper.commodity;

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
public class CommodityInfoWarpper extends BaseControllerWarpper {

    public CommodityInfoWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("commodityTypeName", ConstantFactory.me().getCommodityInfoType(map.get("commodityType").toString()));
    }
}
