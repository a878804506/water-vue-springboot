package cn.enilu.flash.bean.constant.cache;

/**
 * 所有缓存名称的集合
 *
 * @author fengshuonan
 * @date 2017-04-24 21:56
 */
public interface Cache {

    /**
     * 缓存常量，永不过期
     */
    String CONSTANT = "CONSTANT";
    String APPLICATION = "APPLICATION";

    /**
     * 缓存用户信息、token信息 过期时间 见 {JwtUtil.EXPIRE_TIME}
     */
    String SESSION = "SESSION";

    // 过期时间 4 小时
    long SESSION_EXPIRE_TIME = 4 * 60 * 60 * 1000;

    /**
     * 自定义的缓存集合容器key
     */
    String MYKEY = "CYH";

    /**
     * 自定义的缓存集合容器里面的每一次缓存统一的过期时间  一天
     */
    Long MYKEY_EXPIRE_TIME = 60 * 60 * 24 * 1000L;
}
