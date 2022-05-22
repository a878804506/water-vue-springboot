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

    /**
     * 将Double转换成Char数组
     *
     * @param index  返回char长度
     * @param number 待转换的数字
     * @return Char数组（靠右对齐，左边补齐空格）
     */
    public static char[] DoubleToCharArray(int index, Double number) {
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
}
