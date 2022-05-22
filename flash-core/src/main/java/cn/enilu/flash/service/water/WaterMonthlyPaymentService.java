package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.dao.water.WaterCustomerRepository;
import cn.enilu.flash.dao.water.WaterInfoRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.water.NumToCNMoneyUtil;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class WaterMonthlyPaymentService extends BaseService<WaterInfo, Long, WaterInfoRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${water.Excel.root.path}")
    private String waterExcelRootPath;

    @Autowired
    private WaterInfoRepository waterInfoRepository;
    @Autowired
    private WaterCustomerRepository waterCustomerRepository;

    /**
     * 为客户生成账单
     *
     * @param months    包月数
     * @param cid       客户id
     * @param price     包月单价
     * @param half      是否四舍五入
     * @param times     时间段（中文）
     * @return 账单实体
     */
    @Transactional
    public WaterBill createWaterInfo(Integer months, Integer cid, Double price, boolean half, String times) {
        WaterBill waterBill = new WaterBill();
        WaterCustomer waterCustomer = waterCustomerRepository.getOne(Long.valueOf(cid));
        waterBill.setCid(cid);
        waterBill.setCname(waterCustomer.getName());
        waterBill.setPrice(price);
        waterBill.setMonth(months);
        waterBill.setTimes(times);
        waterBill.setAddress(StringUtils.isEmpty(waterCustomer.getAddress()) ? "大峪桥" : waterCustomer.getAddress());
        waterBill.setCapacityCost(new BigDecimal(months * price).setScale(half ? 0 : 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        waterBill.setWaterCost(waterBill.getCapacityCost());
        // double 转换成 char数组
        waterBill.setCharCapacityCost(WaterCommonUtil.DoubleToCharArray(8, waterBill.getCapacityCost()));
        waterBill.setCharWaterCost(WaterCommonUtil.DoubleToCharArray(8, waterBill.getWaterCost()));
        waterBill.setCapitalization(NumToCNMoneyUtil.number2CNMontrayUnit(waterBill.getWaterCost()));
        // 包月水费不需要更改表码值

        this.saveWaterInfo(waterBill);
        return waterBill;
    }

    /**
     * 插入开票记录
     * @param waterBill
     */
    @Transactional
    public void saveWaterInfo(WaterBill waterBill){
        WaterInfo waterInfo = new WaterInfo();
        waterInfo.setCid(waterBill.getCid());
        waterInfo.setCname(waterBill.getCname());
        waterInfo.setMonth(waterBill.getMonth());
        waterInfo.setCount(waterBill.getPrice());
        waterInfo.setCost(waterBill.getWaterCost());
        waterInfo.setRemark(waterBill.getTimes());
        waterInfo.setType(2);
        waterInfoRepository.save(waterInfo);
    }
}

