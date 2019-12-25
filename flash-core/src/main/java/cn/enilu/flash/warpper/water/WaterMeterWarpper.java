package cn.enilu.flash.warpper.water;

import cn.enilu.flash.service.system.impl.ConstantFactory;
import cn.enilu.flash.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WaterMeterWarpper
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2019-12-16 18:03
 **/
public class WaterMeterWarpper extends BaseControllerWarpper {

    public WaterMeterWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("deleteName", ConstantFactory.me().getCustomerDeleteName((Integer) map.get("cdelete")));
    }
}
