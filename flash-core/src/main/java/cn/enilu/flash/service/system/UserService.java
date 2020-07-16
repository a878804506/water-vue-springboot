package cn.enilu.flash.service.system;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.cache.impl.RedisCacheDao;
import cn.enilu.flash.dao.system.UserRepository;
import cn.enilu.flash.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/23 0023.
 *
 * @author enilu
 */
@Service
public class UserService  extends BaseService<User,Long,UserRepository> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisCacheDao redisCacheDao;

    public User findByAccount(String account) {
        //由于：@Cacheable标注的方法，如果其所在的类实现了某一个接口，那么该方法也必须出现在接口里面，否则cache无效。
        //具体的原因是， Spring把实现类装载成为Bean的时候，会用代理包装一下，所以从Spring Bean的角度看，只有接口里面的方法是可见的，其它的都隐藏了，自然课看不到实现类里面的非接口方法，@Cacheable不起作用。
        //所以这里手动控制缓存
        User user =  redisCacheDao.hget(Cache.SESSION,account,User.class);
        if(user!=null){
            return user;
        }
        user = userRepository.findByAccount(account);
        if(null == user){
            return null;
        }
        redisCacheDao.hset(Cache.SESSION,account,user);
        return user;
    }

    @Override
    public User update(User record) {
        User user =  super.update(record);
        redisCacheDao.hset(Cache.SESSION,user.getAccount(),user);
        return user;
    }
}
