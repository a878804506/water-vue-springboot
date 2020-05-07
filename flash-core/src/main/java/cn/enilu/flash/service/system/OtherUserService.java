package cn.enilu.flash.service.system;

import cn.enilu.flash.bean.entity.system.OtherUser;
import cn.enilu.flash.dao.system.OtherUserRepository;
import cn.enilu.flash.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 20200430
 *
 * @author cyh
 */
@Service
public class OtherUserService extends BaseService<OtherUser, Long, OtherUserRepository> {
    @Autowired
    private OtherUserRepository otherUserRepository;

    public OtherUser findByOpenidAndType(String openid, Integer type) {
        return otherUserRepository.findByOpenidAndType(openid, type);
    }

    public OtherUser findByOpenid(String openid) {
        return otherUserRepository.findByOpenid(openid);
    }

    @Transactional
    public int updateByOpenId(Long userid, String openid) {
        return otherUserRepository.updateByOpenId(userid,openid);
    }
}
