package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.dao.water.WaterCustomerRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterCustomerService extends BaseService<WaterCustomer, Long, WaterCustomerRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaterCustomerRepository waterCustomerRepository;

}

