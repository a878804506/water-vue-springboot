package cn.enilu.flash.warpper.water;

import cn.enilu.flash.service.system.impl.ConstantFactory;
import cn.enilu.flash.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WaterCustomerWarpper
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2019-12-16 18:03
 **/
public class WaterCustomerWarpper extends BaseControllerWarpper {

    public WaterCustomerWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getCustomerStatusName((Integer) map.get("status")));

    }
}
