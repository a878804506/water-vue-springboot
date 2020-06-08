package cn.enilu.flash.api.controller.music;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.constant.water.WaterTemplateSQLConstant;
import cn.enilu.flash.bean.entity.music.MusicSync;
import cn.enilu.flash.bean.entity.water.WaterMeter;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.music.MusicSyncTaskService;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 音乐同步任务接口
 * @ClassName MusicSyncController
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 17:28
 **/

@RestController
@RequestMapping("/musicSyncTask")
public class MusicSyncTaskController {

    @Autowired
    private MusicSyncTaskService musicSyncTaskService;

    /**
     * 音乐同步任务列表
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MUSIC_SYNC})
    public Object list(@RequestParam(required = false) String name,@RequestParam(required = false) Integer syncStatus) {
        Page<MusicSync> page = new PageFactory<MusicSync>().defaultPage();
        if (StringUtil.isNotEmpty(name)) {
            // 模糊搜索
            page.addFilter(SearchFilter.build("name", SearchFilter.Operator.LIKE, name));
        }else{
            name = "";
        }
        if(syncStatus != null && syncStatus != -1){
            page.addFilter(SearchFilter.build("syncStatus", SearchFilter.Operator.EQ, syncStatus));
        }
        // 手动排序 id正序
        page.setSort(new Sort(Sort.Direction.DESC,"createTime"));
        name = WaterTemplateSQLConstant.PER_CENT + name + WaterTemplateSQLConstant.PER_CENT;
        page = musicSyncTaskService.queryPage(page);
        return Rets.success(page);
    }
}
