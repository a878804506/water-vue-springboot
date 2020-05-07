package cn.enilu.flash.dao.system;


import cn.enilu.flash.bean.entity.system.OtherUser;
import cn.enilu.flash.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created  on 20200430
 *
 * @author cyh
 */
public interface OtherUserRepository extends BaseRepository<OtherUser,Long> {

    OtherUser findByOpenidAndType(String openid, Integer type);

    OtherUser findByOpenid(String openid);

    @Modifying
    @Query(value = "update t_sys_other_user t set t.userid = ?1  where t.openid = ?2",nativeQuery = true)
    int updateByOpenId(Long userid, String openid);
}
