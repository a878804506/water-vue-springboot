package cn.enilu.flash.security;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.utils.HttpUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author ：enilu
 * @date ：Created in 2019/7/30 22:56
 */
public class JwtUtil {

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        if (StringUtils.isEmpty(token))
            return null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getUserId() {
        return getUserId(HttpUtil.getToken());
    }

    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,xxx后过期
     *
     * @param user 用户
     * @return 加密的token
     */
    public static String sign(User user, boolean isSystemUser, String openId) {
        try {
            Date date = new Date(System.currentTimeMillis() + Cache.SESSION_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
            // 附带username信息
            return JWT.create()
                    .withClaim("isSystemUser", isSystemUser)
                    .withClaim("openId", openId)
                    .withClaim("username", user.getAccount())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    //判断是不是系统登录账号
    public static Boolean checkIsSystemUser(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("isSystemUser").asBoolean();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取第三方登录用户的openId
     *
     * @param token
     * @return
     */
    public static String getOtherLoginUserOpenId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("openId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
