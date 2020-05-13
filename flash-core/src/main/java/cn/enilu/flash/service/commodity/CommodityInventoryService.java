package cn.enilu.flash.service.commodity;


import cn.enilu.flash.bean.entity.commodity.CommodityInOutLog;
import cn.enilu.flash.bean.entity.commodity.CommodityInventory;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.dao.commodity.CommodityInOutLogRepository;
import cn.enilu.flash.dao.commodity.CommodityInventoryRepository;

import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CommodityInventoryService extends BaseService<CommodityInventory, Long, CommodityInventoryRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommodityInventoryRepository commodityInventoryRepository;
    @Autowired
    private CommodityInOutLogRepository commodityInOutLogRepository;

    /**
     * 商品出入库操作
     *
     * @param commodityInventory 商品出入库实体
     * @param type               操作类型 1：入库  2：出库
     * @return
     */
    @Transactional
    public Ret inOrOutInventory(CommodityInventory commodityInventory, int type, String commodityRemarks) {
        CommodityInventory oldCommodityInventory = commodityInventoryRepository.getOne(commodityInventory.getId());
        if (!oldCommodityInventory.getCommodityVersion().equals(commodityInventory.getCommodityVersion())) {
            return Rets.failure("数据过期，请刷新页面！");
        }
        // 当前库存
        int oldCommodityNum = oldCommodityInventory.getCommodityNum();
        // 待修改的库存
        int commodityNum = commodityInventory.getCommodityNum();

        commodityInventory.setModifyBy(JwtUtil.getUserId());
        commodityInventory.setModifyTime(new Date());
        commodityInventory.setCommodityInfo(oldCommodityInventory.getCommodityInfo());
        commodityInventory.setCommodityVersion(oldCommodityInventory.getCommodityVersion() + 1);

        switch (type) {
            case 1:
                // 入库
                if (oldCommodityNum + commodityNum < oldCommodityNum) {
                    return Rets.failure("入库失败，请检查入库是否正确！");
                }
                commodityInventory.setCommodityNum(oldCommodityNum + commodityNum);
                // 更新库存
                this.update(commodityInventory);
                break;
            case 2:
                // 出库
                if (oldCommodityNum - commodityInventory.getCommodityNum() > oldCommodityNum || oldCommodityNum - commodityInventory.getCommodityNum() < 0) {
                    return Rets.failure("出库失败，请检查出库是否正确！");
                }
                commodityInventory.setCommodityNum(oldCommodityNum - commodityNum);
                // 更新库存
                this.update(commodityInventory);
                break;
        }
        // 新增出入库记录
        CommodityInOutLog commodityInOutLog = new CommodityInOutLog();
        commodityInOutLog.setCommodityInfo((oldCommodityInventory.getCommodityInfo()));
        commodityInOutLog.setCommodityInOutType(type);
        commodityInOutLog.setCommodityInOutNum(commodityNum);
        commodityInOutLog.setCommodityTradePrice(oldCommodityInventory.getCommodityInfo().getCommodityTradePrice());
        commodityInOutLog.setCommoditySalesPrice(oldCommodityInventory.getCommodityInfo().getCommoditySalesPrice());
        commodityInOutLog.setCommodityRemarks(commodityRemarks);
        commodityInOutLogRepository.save(commodityInOutLog);

        return Rets.success();
    }
}

