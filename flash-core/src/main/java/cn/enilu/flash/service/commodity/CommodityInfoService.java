package cn.enilu.flash.service.commodity;


import cn.enilu.flash.bean.entity.commodity.CommodityInfo;
import cn.enilu.flash.bean.entity.commodity.CommodityInventory;
import cn.enilu.flash.dao.commodity.CommodityInfoRepository;

import cn.enilu.flash.dao.commodity.CommodityInventoryRepository;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommodityInfoService extends BaseService<CommodityInfo,Long,CommodityInfoRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommodityInfoRepository commodityInfoRepository;
    @Autowired
    private CommodityInventoryRepository commodityInventoryRepository;

    @Override
    public CommodityInfo insert(CommodityInfo commodityInfo) {
        // 添加商品信息
        commodityInfo = commodityInfoRepository.save(commodityInfo);
        //初始化商品库存信息
        CommodityInventory commodityInventory = new CommodityInventory();
        commodityInventory.setCommodityInfo(commodityInfo);
        commodityInventory.setCommodityNum(0);
        commodityInventory.setCommodityVersion(1);
        commodityInventoryRepository.save(commodityInventory);
        return commodityInfo;
    }

    @Transactional
    public void delete(Long id) {
        commodityInfoRepository.deleteById(id);
        commodityInventoryRepository.deleteByCommodityInfoId(id);
    }
}

