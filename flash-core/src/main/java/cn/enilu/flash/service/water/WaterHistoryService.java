package cn.enilu.flash.service.water;

import cn.enilu.flash.bean.constant.water.WaterConstant;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.dao.system.UserRepository;
import cn.enilu.flash.dao.water.WaterHistoryRepository;
import cn.enilu.flash.dao.water.WaterMeterRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.utils.water.WaterCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WaterHistoryService extends BaseService<WaterMeter, Long, WaterMeterRepository> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaterHistoryRepository waterHistoryRepository;

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
        records.forEach( waterinfo -> {
            waterinfo.setModifyName(userMapping.getOrDefault(waterinfo.getModifyBy(), ""));
        });
        String excu_page_count_sql = WaterCommonUtil.replaceTemplateSQL(WaterTemplateSQLConstant.WATER_SYS_PAGE_COUNT, WaterConstant.WATER_INFO,WaterConstant.SYS_USER, name);
        page.setRecords(records);
        page.setTotal(Integer.valueOf(waterHistoryRepository.getBySql(excu_page_count_sql) + ""));
        return page;
    }

    public Map<String, Object> monthStatistics(int year, int month) {
        Map<String, Object> result = new HashMap<>();

        // 统计出当月已经开票的
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(WaterConstant.YYYY_MM_DD_HH_MM_SS);
        LocalDateTime date = LocalDateTime.of(year, month, 1, 0 , 0, 0);
        LocalDateTime firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime lastDay = date.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        List<Map<String, Object>> waterInfos = waterHistoryRepository.queryWaterInfoByMonth(fmt.format(firstday), fmt.format(lastDay));
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> waterInfo : waterInfos){
            total = total.add(new BigDecimal(waterInfo.get("cost").toString()));
        }
        result.put("waterInfo", waterInfos);
        result.put("total", total.doubleValue());

        // 统计出当月没有开票的用户信息
        List<Map<String, Object>> waterCustomers = waterHistoryRepository.queryNotMonthBillCustomers(fmt.format(firstday), fmt.format(lastDay));
        result.put("waterCustomer", waterCustomers);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void waterCancel(int id, String remark, String reason) {
        waterHistoryRepository.cancelBill(id, remark, reason);
    }
}

