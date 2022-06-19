package cn.enilu.flash.api.controller.water;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.water.WaterHistoryService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/water/waterhistory")
public class WaterHistoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaterHistoryService waterHistoryService;

    /**
     * 查询开票历史
     *
     * @param cname
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String cname) {
        Page<WaterInfo> page = new PageFactory<WaterInfo>().defaultPage();
        if (StringUtil.isNotEmpty(cname)) {
            // 模糊搜索
            page.addFilter(SearchFilter.build("cname", SearchFilter.Operator.LIKE, cname));
        }else{
            cname = "";
        }
        // 手动排序 id正序
        page.setSort(new Sort(Sort.Direction.ASC,"id"));
        cname = WaterTemplateSQLConstant.PER_CENT + cname + WaterTemplateSQLConstant.PER_CENT;
        page = waterHistoryService.queryWaterInfoPage(page, cname);
        return Rets.success(page);
    }

    /**
     * 月度统计
     *
     * @param year 年份
     * @param month 月份
     * @return 月度统计信息
     */
    @RequestMapping(value = "/monthStatistics", method = RequestMethod.GET)
    public Object monthStatistics(@RequestParam int year, @RequestParam int month) {
        Map<String, Object> result = waterHistoryService.monthStatistics(year, month);
        return Rets.success(result);
    }

    /**
     * 作废单据
     *
     * @param id 用戶id
     * @param reason 作废原因
     * @return 月度统计信息
     */
    @RequestMapping(value = "/waterCancel", method = RequestMethod.POST)
    public Object waterCancel(int id, String remark, String reason) {
        waterHistoryService.waterCancel(id, remark, reason);
        return Rets.success();
    }
}