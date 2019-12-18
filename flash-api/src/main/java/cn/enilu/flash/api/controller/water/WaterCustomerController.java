package cn.enilu.flash.api.controller.water;

import cn.enilu.flash.bean.entity.water.WaterCustomer;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.water.WaterCustomerService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;

import cn.enilu.flash.warpper.water.WaterCustomerWarpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/water/customer")
public class WaterCustomerController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaterCustomerService waterCustomerService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String delete) {
        Page page = new PageFactory().defaultPage();
        if (StringUtil.isNotEmpty(name)) {
            // 模糊搜索
            page.addFilter(SearchFilter.build("name", SearchFilter.Operator.LIKE, name));
        }
        if (StringUtil.isNotEmpty(status)) {
            page.addFilter(SearchFilter.build("status", SearchFilter.Operator.EQ, status));
        }
        if (StringUtil.isNotEmpty(delete)) {
            page.addFilter(SearchFilter.build("delete", SearchFilter.Operator.EQ, delete));
        }
        // 手动排序 id正序
        Sort sort = new Sort("id");
        sort.ascending();
        page.setSort(sort);
        page = waterCustomerService.queryPage(page);
        // 字典字段转换
        List list = (List) new WaterCustomerWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp();
        page.setRecords(list);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "新增、编辑客户信息表", key = "name", dict = CommonDict.class)
    public Object save(@ModelAttribute WaterCustomer tWaterCustomer) {
        if (tWaterCustomer.getId() == null) {
            waterCustomerService.insert(tWaterCustomer);
        } else {
            waterCustomerService.update(tWaterCustomer);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除客户信息表", key = "id", dict = CommonDict.class)
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        waterCustomerService.delete(id);
        return Rets.success();
    }
}