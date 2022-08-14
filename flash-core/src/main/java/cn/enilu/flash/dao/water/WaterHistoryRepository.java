package cn.enilu.flash.dao.water;

import cn.enilu.flash.bean.entity.water.WaterInfo;
import cn.enilu.flash.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface WaterHistoryRepository extends BaseRepository<WaterInfo, Long> {

    @Query(value = "select t.*,t1.name modifyName from t_water_waterinfo t left join t_sys_user t1 on t.modify_by = t1.id where t.cname like ?1 order by t.modify_time desc limit ?2,?3",nativeQuery = true)
    List<WaterInfo> queryWaterInfoPage(String name, int current, int size);

    @Query(value = "SELECT t.cid id, t.cname name, t.cost, t.remark, t.modify_time modifyTime, t1.name modifyName, t2.address  FROM t_water_waterinfo t LEFT JOIN t_sys_user t1 ON t.modify_by = t1.id left join t_water_customer t2 on t.cid = t2.id WHERE t.modify_time BETWEEN DATE_FORMAT( ?1,'%Y-%m-%d %H:%i:%s')  and  DATE_FORMAT( ?2 ,'%Y-%m-%d %H:%i:%s') and cancel = 0 ORDER BY t.modify_time DESC ",nativeQuery = true)
    List<Map<String, Object>> queryWaterInfoByMonth(String startTime, String endTime);

    @Query(value = "select t.id, t.name, t.price, t.address, t.starttime, t.status from t_water_customer t where t.id not in (SELECT t.cid  FROM t_water_waterinfo t  WHERE t.modify_time BETWEEN DATE_FORMAT( ?1 ,'%Y-%m-%d %H:%i:%s')  and  DATE_FORMAT( ?2 ,'%Y-%m-%d %H:%i:%s') );",nativeQuery = true)
    List<Map<String, Object>> queryNotMonthBillCustomers(String format, String format1);

    @Modifying
    @Query(value = "update t_water_waterinfo set cancel = 1, reason = ?3 where cid = ?1 and remark = ?2",nativeQuery = true)
    int cancelBill(int id, String remark, String reason);

    @Query(value = "SELECT t.*, t2.address  FROM t_water_waterinfo t left join t_water_customer t2 on t.cid = t2.id WHERE t.modify_time BETWEEN DATE_FORMAT( ?1,'%Y-%m-%d %H:%i:%s')  and  DATE_FORMAT( ?2 ,'%Y-%m-%d %H:%i:%s') and cancel = 0 and t.modify_by = ?3 ORDER BY t.modify_time DESC ",nativeQuery = true)
    List<WaterInfo> queryWaterInfoByUserId(String startTime, String endTime, Long loginUserId);
}

