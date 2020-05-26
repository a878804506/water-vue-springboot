package cn.enilu.flash.service.water;


import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.water.WaterCustomerRepository;
import cn.enilu.flash.dao.water.WaterInfoRepository;
import cn.enilu.flash.dao.water.WaterMeterRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.water.NumToCNMoneyUtil;
import cn.enilu.flash.utils.water.OperateExcelUtil;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class WaterInfoService extends BaseService<WaterInfo, Long, WaterInfoRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${water.Excel.root.path}")
    private String waterExcelRootPath;

    @Autowired
    private WaterInfoRepository waterInfoRepository;
    @Autowired
    private WaterCustomerRepository waterCustomerRepository;
    @Autowired
    private WaterMeterRepository waterMeterRepository;

    /**
     * 为客户生成账单
     *
     * @param month     账单月份
     * @param cid       客户id
     * @param meterCode 本月止码
     * @return 账单实体
     */
    @Transactional
    public WaterBill createWaterInfo(Integer month, Integer cid, Double meterCode) {
        WaterBill waterBill = new WaterBill();
        WaterCustomer waterCustomer = waterCustomerRepository.getOne(Long.valueOf(cid));
        WaterMeter waterMeter = waterMeterRepository.findByCid(cid);
        Double lastMonthWaterCount = this.getLastMonthWaterCount(month, waterMeter, meterCode);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        waterBill.setCid(cid);
        waterBill.setCname(waterCustomer.getName());
        waterBill.setPrice(waterCustomer.getPrice());
        waterBill.setYear(year);
        waterBill.setMonth(month);
        waterBill.setFirstNumber(lastMonthWaterCount);
        waterBill.setLastNumber(new BigDecimal(meterCode).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        waterBill.setWaterCount(new BigDecimal(meterCode - lastMonthWaterCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        waterBill.setMeterageCost(new BigDecimal(waterCustomer.getPrice() * (waterBill.getWaterCount())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        waterBill.setAddress(StringUtils.isEmpty(waterCustomer.getAddress()) ? "紫金" : waterCustomer.getAddress());
        waterBill.setCapacityCost(month == 13 ? 10d : 5d);
        waterBill.setWaterCost(waterBill.getMeterageCost() + waterBill.getCapacityCost());
        // double 转换成 char数组
        waterBill.setCharMeterageCost(this.DoubleToCharArray(8, waterBill.getMeterageCost()));
        waterBill.setCharCapacityCost(this.DoubleToCharArray(8, waterBill.getCapacityCost()));
        waterBill.setCharWaterCost(this.DoubleToCharArray(8, waterBill.getWaterCost()));
        waterBill.setCapitalization(NumToCNMoneyUtil.number2CNMontrayUnit(waterBill.getWaterCost()));
        waterBill.setExcelFileName(OperateExcelUtil.createExcelFile(waterBill, waterExcelRootPath));

        //入库逻辑
        waterMeterRepository.save(waterMeter);
        WaterInfo waterInfo = new WaterInfo();
        waterInfo.setCid(cid);
        waterInfo.setCname(waterCustomer.getName());
        waterInfo.setYear(year);
        waterInfo.setMonth(month);
        waterInfo.setCount(waterBill.getWaterCount());
        waterInfo.setCost(waterBill.getWaterCost());
        waterInfoRepository.save(waterInfo);
        return waterBill;
    }

    /**
     * 将Double转换成Char数组
     *
     * @param index  返回char长度
     * @param number 待转换的数字
     * @return Char数组（靠右对齐，左边补齐空格）
     */
    private char[] DoubleToCharArray(int index, Double number) {
        char[] numberChars = String.format("%.2f", number).toCharArray();
        if (index + 1 < numberChars.length) {
            return new char[]{};
        }
        char[] resultChar = new char[index];
        int temp = index - numberChars.length;
        for (int i = 0; i < temp; i++) {
            resultChar[i] = ' ';
        }
        //将数组1放到目标数组中，参数为：
        // 1.将要复制的数组  2.从将要复制的数组的第几个元素开始  3.目标数组   4.将要放到目标数组的那个位置   5.复制多少个元素
        System.arraycopy(numberChars, 0, resultChar, temp, numberChars.length);
        return resultChar;
    }

    /**
     * 根据月份 获取上个月的起码
     *
     * @param month      月份
     * @param waterMeter 客户起止码实体
     * @param meterCode  客户本月录入的止码
     * @return 上个月的起码
     */
    private Double getLastMonthWaterCount(Integer month, WaterMeter waterMeter, Double meterCode) {
        Double lastMonthWaterCount = 0.0;
        switch (month) {
            case 1:
                lastMonthWaterCount = waterMeter.getThirteen();
                waterMeter.setOne(meterCode);
                break;
            case 2:
                lastMonthWaterCount = waterMeter.getOne();
                waterMeter.setTwo(meterCode);
                break;
            case 3:
                lastMonthWaterCount = waterMeter.getTwo();
                waterMeter.setThree(meterCode);
                break;
            case 4:
                lastMonthWaterCount = waterMeter.getThree();
                waterMeter.setFour(meterCode);
                break;
            case 5:
                lastMonthWaterCount = waterMeter.getFour();
                waterMeter.setFive(meterCode);
                break;
            case 6:
                lastMonthWaterCount = waterMeter.getFive();
                waterMeter.setSix(meterCode);
                break;
            case 7:
                lastMonthWaterCount = waterMeter.getSix();
                waterMeter.setSeven(meterCode);
                break;
            case 8:
                lastMonthWaterCount = waterMeter.getSeven();
                waterMeter.setEight(meterCode);
                break;
            case 9:
                lastMonthWaterCount = waterMeter.getEight();
                waterMeter.setNine(meterCode);
                break;
            case 10:
                lastMonthWaterCount = waterMeter.getNine();
                waterMeter.setTen(meterCode);
                break;
            case 11:
                lastMonthWaterCount = waterMeter.getTen();
                waterMeter.setEleven(meterCode);
                break;
            case 12:
                lastMonthWaterCount = waterMeter.getEleven();
                waterMeter.setTwelve(meterCode);
                break;
            case 13:
                lastMonthWaterCount = waterMeter.getThirteen();
                waterMeter.setTwo(meterCode);
                break;
        }
        return lastMonthWaterCount;
    }

    /**
     * 判断本月止码是否有效
     *
     * @param month
     * @param cid
     * @param meterCode
     * @return
     */
    public Map checkMeterCode(Integer month, Integer cid, Double meterCode) {
        Map result = new HashMap();
        WaterMeter waterMeter = waterMeterRepository.findByCid(cid);
        Double lastMonthWaterCount = this.getLastMonthWaterCount(month, waterMeter, meterCode);
        if (meterCode < lastMonthWaterCount) {
            result.put("result", false);
            result.put("msg", "上月止码是：" + lastMonthWaterCount + "，本月止码是：" + meterCode + "，数据不合法");
            result.put("type", 1);
        } else if (meterCode - lastMonthWaterCount > 50) {
            result.put("result", false);
            result.put("msg", waterMeter.getWaterCustomer().getName() + "的本月用水量大于50吨水，是否继续？");
            result.put("type", 0);
        } else {
            result.put("result", true);
        }
        return result;
    }

    /**
     * 页面初始化后和每次生成完账单后查询开票数量详情
     *
     * @param month 查询月份
     * @return
     */
    public Object getWaterInfo(Integer month) {
        Map result = new HashMap();
        result.put("toDay", waterInfoRepository.countByModifyTimeBetween(WaterCommonUtil.getStartDate(new Date()), WaterCommonUtil.getEndDate(new Date())));
        result.put("toMonth", waterInfoRepository.countByYearAndMonth(Calendar.getInstance().get(Calendar.YEAR), month));
        return result;
    }

    /**
     * 全镇月度总水费详情
     *
     * @param month 月份
     * @return
     */
    public Object getCustomersWaterCostByMonth(Integer month) {
        Map result = new HashMap();
        List<Map> tableData = new ArrayList<>();
        Map oneData = new HashMap();
        List<WaterCustomer> waterCustomers = waterCustomerRepository.findAll();
        oneData.put("waterCustomersCount", waterCustomers.size());
        //报停用户数
        int statusCount = 0;
        for (WaterCustomer waterCustomer : waterCustomers) {
            if (waterCustomer.getStatus() == 0) {
                statusCount++;
            }
        }
        oneData.put("statusCount", statusCount);
        // 应录入用户数
        oneData.put("shouldCustomerCount", waterCustomers.size() - statusCount);
        List<WaterInfo> customersWaterInfo = waterInfoRepository.findByYearAndMonth(Calendar.getInstance().get(Calendar.YEAR), month);
        // 实际入用户数
        int practicalCustomerCount = customersWaterInfo.size();
        oneData.put("practicalCustomerCount", practicalCustomerCount);
        // 未录入的用户数
        oneData.put("noPracticalCustomerCount", waterCustomers.size() - statusCount - practicalCustomerCount);

        // 实际录入总用水量
        Double waterCount = 0d;
        // 实际录入总水费
        Double waterCost = 0d;
        //  实际录入水费的客户id列表
        Set<Integer> customerCid = new HashSet<>();
        for (WaterInfo waterInfo : customersWaterInfo) {
            waterCount += waterInfo.getCount();
            waterCost += waterInfo.getCost();
            customerCid.add(waterInfo.getCid());
        }
        oneData.put("waterCount", new BigDecimal(waterCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        oneData.put("waterCost", new BigDecimal(waterCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        tableData.add(oneData);
        result.put("tableData", tableData);
        // 没有录入当月水费的客户起止码
        List<WaterMeter> practicalCustomerWaterInfo = waterMeterRepository.findByCidNotIn(customerCid);
        result.put("practicalCustomerWaterInfo", practicalCustomerWaterInfo);
        return result;
    }

    public Object getToDayTabs() {
        int toDayCustomers = waterInfoRepository.countByModifyTimeBetween(WaterCommonUtil.getStartDate(new Date()), WaterCommonUtil.getEndDate(new Date()));
        int page = toDayCustomers % 50 == 0 ? toDayCustomers / 50 : toDayCustomers / 50 + 1;
        if(page == 0){
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (int i = 1; i <= page; i++) {
            result.add("第"+((i-1)*50+1)+"---"+(i*50)+"户");
        }
        return result;
    }
}

