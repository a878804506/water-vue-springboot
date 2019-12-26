package cn.enilu.flash.utils.water;

import cn.enilu.flash.utils.StringUtil;

/**
 * 水务系统的 公共方法工具类
 *
 * @ClassName WaterCommonUtile
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2019-12-24 13:54
 **/
public class WaterCommonUtile {

    /**
     * 替换掉模板sql里面的字符串
     *  使用String.format 替换字符串
     * @param sqlStr sql
     * @param params 参数
     * @return
     */
    public static String replaceTemplateSQL(String sqlStr, Object... params) {
        if (StringUtil.isNotNullOrEmpty(sqlStr) && params.length != 0) {
            return String.format(sqlStr, params);
        }
        return null;
    }
}
