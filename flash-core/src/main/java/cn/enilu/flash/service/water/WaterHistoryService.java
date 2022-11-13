package cn.enilu.flash.service.water;

import cn.enilu.flash.bean.constant.water.WaterConstant;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.system.UserRepository;
import cn.enilu.flash.dao.water.WaterCustomerRepository;
import cn.enilu.flash.dao.water.WaterHistoryRepository;
import cn.enilu.flash.dao.water.WaterMeterRepository;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.ExcelUtil;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaterHistoryService extends BaseService<WaterMeter, Long, WaterMeterRepository> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaterHistoryRepository waterHistoryRepository;
    @Autowired
    private WaterCustomerRepository waterCustomerRepository;

    /**
     * WaterMeter 列表查询
     *
     * @param page 分页组件
     * @param name 名字模糊查询
     * @return
     */
    public Page<WaterInfo> queryWaterInfoPage(Page<WaterInfo> page, String name) {
        List<WaterInfo> records = waterHistoryRepository.queryWaterInfoPage(name, (page.getCurrent() - 1) * page.getLimit(), page.getLimit());
        List<User> users = userRepository.query("select * from t_sys_user");
        Map<Long, String> userMapping = users.stream().collect(Collectors.toMap(User::getId, User::getName));
        records.forEach(waterinfo -> {
            waterinfo.setModifyName(userMapping.getOrDefault(waterinfo.getModifyBy(), ""));
        });
        String excu_page_count_sql = WaterCommonUtil.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_SYS_PAGE_COUNT, WaterConstant.WATER_INFO, WaterConstant.SYS_USER, name);
        page.setRecords(records);
        page.setTotal(Integer.valueOf(waterHistoryRepository.getBySql(excu_page_count_sql) + ""));
        return page;
    }

    public Map<String, Object> monthStatistics(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        // 统计出当月已经开票的
        List<Map<String, Object>> waterInfos = waterHistoryRepository.queryWaterInfoByMonth(startDate, endDate);
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> waterInfo : waterInfos) {
            total = total.add(new BigDecimal(waterInfo.get("cost").toString()));
        }
        result.put("waterInfo", waterInfos);
        result.put("total", total.doubleValue());

        // 统计出当月没有开票的用户信息
        List<Map<String, Object>> waterCustomers = waterHistoryRepository.queryNotMonthBillCustomers(startDate, endDate);
        result.put("waterCustomer", waterCustomers);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void waterCancel(int id, String remark, String reason) {
        waterHistoryRepository.cancelBill(id, remark, reason);
    }

    public void monthStatisticsExport(String startDate, String endDate, String token) {
        Long userId = JwtUtil.getUserId(token);
        String userNike = JwtUtil.getUserNike(token);
        // 统计出当月已经开票的
        List<WaterInfo> waterInfos = waterHistoryRepository.queryWaterInfoByUserId(startDate, endDate, userId);
        List<WaterCustomer> allWaterCustomer = waterCustomerRepository.findAll();

        BigDecimal total = BigDecimal.ZERO;
        for (WaterInfo waterInfo : waterInfos) {
            total = total.add(new BigDecimal(waterInfo.getCost()));
            allWaterCustomer.forEach(customer -> {
                if (customer.getId().intValue() == waterInfo.getCid()) {
                    waterInfo.setAddress(customer.getAddress());
                }
            });
        }
        List<Map<?, ?>> notAddress_waterInfos = new ArrayList<>();
        List<Map<?, ?>> list = new ArrayList<>();
        waterInfos.forEach(waterInfo -> list.add(BeanUtil.beanToMapDiy(waterInfo, userNike)));
        Map<String, List<Map<?, ?>>> collect = list.stream().filter(item -> {
            if (item.containsKey("address")) {
                return true;
            } else {
                notAddress_waterInfos.add(item);
                return false;
            }
        }).collect(Collectors.groupingBy(e -> e.get("address").toString()));

        List<Map<String, Object>> groupCosts = new LinkedList<>();
        BigDecimal allCost = new BigDecimal("0");
        Iterator<String> iterator = collect.keySet().iterator();
        while (iterator.hasNext()) {
            String address = iterator.next();
            BigDecimal totalByAddress = new BigDecimal("0");
            for (WaterInfo waterInfo : waterInfos) {
                if (address.equals(waterInfo.getAddress())) {
                    BigDecimal bigDecimal = new BigDecimal(waterInfo.getCost());
                    totalByAddress = totalByAddress.add(bigDecimal);
                    allCost = allCost.add(bigDecimal);
                }
            }
            Map<String, Object> addressGroupCost = new HashMap<>();
            addressGroupCost.put("address", address);
            addressGroupCost.put("totalCost", totalByAddress.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            groupCosts.add(addressGroupCost);
        }
        Map<String, Object> allCostMap = new HashMap<>();
        allCostMap.put("address", "总计");
        allCostMap.put("totalCost", allCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        groupCosts.add(allCostMap);


        // 转？，？ ，出excel 即可
        ExcelUtil.ExportExcel(collect, groupCosts, startDate, endDate, userNike);
    }

    public void monthStatisticsOriginExport(String startDate, String endDate, String token) {
        Long userId = JwtUtil.getUserId(token);
        String userNike = JwtUtil.getUserNike(token);
        // 统计出当月已经开票的
        List<WaterInfo> waterInfos = waterHistoryRepository.queryWaterInfoByUserId(startDate, endDate, userId);

        List<WaterCustomer> allWaterCustomer = waterCustomerRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;
        for (WaterInfo waterInfo : waterInfos) {
            total = total.add(new BigDecimal(waterInfo.getCost()));
            allWaterCustomer.forEach(customer -> {
                if (customer.getId().intValue() == waterInfo.getCid()) {
                    waterInfo.setAddress(customer.getAddress());
                }
            });
        }

        List<Map<?, ?>> list = new ArrayList<>();
        waterInfos.forEach(waterInfo -> list.add(BeanUtil.beanToMapDiy(waterInfo, userNike)));
        BigDecimal allCost = new BigDecimal("0");
        for (WaterInfo waterInfo : waterInfos) {
            BigDecimal bigDecimal = new BigDecimal(waterInfo.getCost());
            allCost = allCost.add(bigDecimal);
        }
        // 转？，？ ，出excel 即可
        ExcelUtil.ExportOriginExcel(list, startDate, endDate, userNike, allCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}

