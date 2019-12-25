package cn.enilu.flash.bean.constant.water;

/**
 * 水务系统 模板 SQL
 *
 * @ClassName WaterConstant
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2019-12-23 16:58
 **/
public class WaterTemplateSQLConstant {

    /**
     * 模板替换字符串
     */
    public static final String REPLACE_STR = "?";

    /**
     * 分页查询分表的sql模板
     * 对应数据库中的表 t_water_watermeterXXXX
     * 注释：需要拼接年份使用
     */
    public static final String WATER_METTER_PAGE = "select * from ?1  where cname like '%?2%' limit ?3,?4";

    /**
     * 分页查询分表s数量的sql模板
     */
    public static final String WATER_METTER_PAGE_COUNT = "select count(1) from ?1  where cname like '%?2%' ";

    public static final String WATER_METTER_UPDATE = "update ?1 set one=?2 ,two=?3,three=?4,four=?5,five=?6,six=?7,seven=?8,eight=?9,nine=?10,ten=?11,eleven=?12,twelve=?13 where id=?14";
}
