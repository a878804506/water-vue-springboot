package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.water.WaterCustomerRepository;

import cn.enilu.flash.dao.water.WaterMeterRepository;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaterCustomerService extends BaseService<WaterCustomer, Long, WaterCustomerRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaterMeterService waterMeterService;
    @Autowired
    private WaterMeterRepository waterMeterRepository;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public WaterCustomer insertCustomerAndMeter(WaterCustomer waterCustomer) {
        waterCustomer = this.insert(waterCustomer);
        WaterMeter waterMeter = new WaterMeter(waterCustomer.getId().intValue(),waterCustomer.getName());
        waterMeterService.insert(waterMeter);
        return waterCustomer;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateCustomerAndMeter(WaterCustomer waterCustomer) {
        waterCustomer = this.update(waterCustomer);
        WaterMeter waterMeter = waterMeterRepository.findByCid(waterCustomer.getId().intValue());
        if(!waterMeter.getCname().equals(waterCustomer.getName())){
            waterMeter.setCname(waterCustomer.getName());
            waterMeterService.update(waterMeter);
        }
    }
}

