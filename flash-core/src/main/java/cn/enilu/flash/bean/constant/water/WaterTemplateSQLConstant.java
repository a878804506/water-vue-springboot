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
     * 模糊查询 包装参数
     */
    public static final String PER_CENT = "%";

    /**
     * 分页查询分表的sql模板
     * 对应数据库中的表 t_water_watermeterXXXX
     * 注释：需要拼接年份使用
     */
    public static final String WATER_PAGE = "select * from %s t , %s t1 where t.cid = t1.id and t1.name like '%s' limit %s,%s";

    /**
     * 分页查询分表s数量的sql模板
     */
    public static final String WATER_PAGE_COUNT = "select count(1) from %s t , %s t1 where t.cid = t1.id and t1.name like '%s'";

    public static final String WATER_METTER_UPDATE = "update %s set one=%s ,two=%s,three=%s,four=%s,five=%s,six=%s,seven=%s,eight=%s,nine=%s,ten=%s,eleven=%s,twelve=%s where id=%s";

    public static final String WATER_SYS_PAGE = "select t.*,t1.name modifyName from %s t left join %s t1 on t.modify_by = t1.id where t.cname like '%s' order by t.modify_time desc limit %s,%s";

    public static final String WATER_SYS_PAGE_COUNT = "select count(1) from %s t left join  %s t1 on t.modify_by = t1.id where t.cname like '%s' order by t.modify_time desc";
}
