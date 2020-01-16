package cn.enilu.flash.utils.water;

import cn.enilu.flash.utils.StringUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 水务系统的 公共方法工具类
 *
 * @ClassName WaterCommonUtile
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2019-12-24 13:54
 **/
public class WaterCommonUtil {

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

    public static Date getStartDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);;
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    public static Date getEndDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);;
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }
}
