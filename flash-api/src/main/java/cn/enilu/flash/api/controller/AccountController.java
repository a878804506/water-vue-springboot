package cn.enilu.flash.api.controller;

import cn.enilu.flash.api.utils.ApiConstants;
import cn.enilu.flash.bean.constant.Const;
import cn.enilu.flash.bean.constant.Constant;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.system.OtherUser;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.core.log.LogManager;
import cn.enilu.flash.core.log.LogTaskFactory;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.security.ShiroFactroy;
import cn.enilu.flash.service.system.OtherUserService;
import cn.enilu.flash.service.system.UserService;
import cn.enilu.flash.utils.*;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AccountController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OtherUserService otherUserService;

    @Autowired
    private TokenCache tokenCache;

    @Value("${dingding.getUserInfoByTempCode}")
    private String dingDingGetUserInfoURL;

    @Value("${dingding.appId}")
    private String dingDingAppId;

    @Value("${dingding.login}")
    private Boolean dingDingLogin;

    @Value("${dingding.appSecret}")
    private String dingdingAppSecret;

    /**
     * 用户登录<br>
     * 1，验证没有注册<br>
     * 2，验证密码错误<br>
     * 3，登录成功
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam("username") String userName,
                        @RequestParam("password") String password) {
        try {
            logger.info("用户登录:" + userName + ",密码:" + password);
            //1,
            User user = userService.findByAccount(userName);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }
            if (user.getId().intValue() < 1) {
                return Rets.failure("不能登录系统初始用户");
            }

            String passwdMd5 = MD5.md5(password, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("输入的密码错误");
            }

            String token = JwtUtil.sign(user,true,null);
            Map<String, String> result = new HashMap<>(1);
            logger.info("token:{}", token);
            result.put("token", token);
            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));

            ShiroUser shiroUser = ShiroFactroy.me().createShiroUser(user);
            tokenCache.setUser(token,shiroUser);

            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object info() {
        HttpServletRequest request = HttpUtil.getRequest();
        Long idUser = null;
        String token = getToken(request);
        Boolean isSystemUser ;
        try {
            idUser = getIdUser(request);
            isSystemUser = JwtUtil.checkIsSystemUser(token);
        } catch (Exception e) {
            return Rets.expire();
        }
        if (idUser != null) {
            User user = userService.get(idUser);
            if (StringUtil.isEmpty(user.getRoleid())) {
                return Rets.failure("该用户未配置权限");
            }
            if(!isSystemUser){
                OtherUser otherUser = otherUserService.findByOpenid(JwtUtil.getOtherLoginUserOpenId(token));
                // 第三方登录 且没有绑定本系统的账号 名称显示昵称
                user.setName(otherUser.getNick());
            }
            ShiroUser shiroUser = tokenCache.getUser(token);
            if(null == shiroUser){
                return Rets.expire();
            }

            Map map = Maps.newHashMap("name", user.getName(), "role", "admin", "roles", shiroUser.getRoleCodes());
            map.put("permissions", shiroUser.getUrls());
            Map profile = (Map) Mapl.toMaplist(user);
            profile.put("dept", shiroUser.getDeptName());
            profile.put("roles", shiroUser.getRoleNames());
            map.put("profile", profile);

            return Rets.success(map);
        }
        return Rets.failure("获取用户信息失败");
    }

    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public Object updatePwd(String oldPassword, String password, String rePassword) {
        try {
            HttpServletRequest request = HttpUtil.getRequest();
            String token = getToken(request);
            Boolean isSystemUser = JwtUtil.checkIsSystemUser(token);
            if(!isSystemUser){
                return Rets.failure("第三方登录的用户请先绑定账号！");
            }

            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return Rets.failure("密码不能为空");
            }
            if (!password.equals(rePassword)) {
                return Rets.failure("新密码前后不一致");
            }
            User user = userService.get(getIdUser(HttpUtil.getRequest()));
            /*if (ApiConstants.ADMIN_ACCOUNT.equals(user.getAccount())) {
                return Rets.failure("不能修改超级管理员密码");
            }*/
            logger.info("oldPassword:{},password:{},rePassword:{}", MD5.md5(oldPassword, user.getSalt()), password, rePassword);
            if (!MD5.md5(oldPassword, user.getSalt()).equals(user.getPassword())) {
                return Rets.failure("旧密码输入错误");
            }

            user.setPassword(MD5.md5(password, user.getSalt()));
            userService.update(user);
            return Rets.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("更改密码失败");
    }


    @RequestMapping(value = "/dingdingCallback", method = RequestMethod.GET)
    public Object dingdingCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        if(null == dingDingLogin || dingDingLogin == false)
            return Rets.failure("禁止钉钉扫码登陆！");

        if( StringUtil.isEmpty(code) )
            return Rets.failure("无效的临时授权码！");

        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(dingDingGetUserInfoURL);
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(code);
            OapiSnsGetuserinfoBycodeResponse response = client.execute(req,dingDingAppId,dingdingAppSecret);
            if(!response.isSuccess())
                return Rets.failure("登录时失败:"+response.getErrmsg());

            // 1=钉钉登陆
            OtherUser loginUser = otherUserService.findByOpenidAndType(response.getUserInfo().getOpenid(), Constant.OTHER_LOGIN_DINGDING);

            if(null == loginUser){
                // 钉钉扫码第一次登陆 ,记录到 other_user表
                loginUser = new OtherUser();
                loginUser.setOpenid(response.getUserInfo().getOpenid());
                loginUser.setUnionid(response.getUserInfo().getUnionid());
                loginUser.setNick(response.getUserInfo().getNick());
                loginUser.setType(Constant.OTHER_LOGIN_DINGDING);
                loginUser.setCreateTime(new Date());
                otherUserService.insert(loginUser);
            }

            Map<String, Object> result = new HashMap<>(1);
            String token ;
            User user ;
            if(null != loginUser.getUserid()){
                // 钉钉登陆用户 已经绑定到了系统用户中 ，那么就用系统用户登录系统
                user =  userService.get(Long.valueOf(loginUser.getUserid()));
                token = JwtUtil.sign(user,true ,null);
                result.put("isSystemUser",true);
            }else{
                // 钉钉登录的用户 未绑定系统用户，则作为访客登陆
                user =  userService.get(Long.valueOf(Const.OTHER_LOGIN_USER_ID));
                token = JwtUtil.sign(user,false ,loginUser.getOpenid());
                result.put("isSystemUser",false);
            }
            logger.info("token:{}", token);
            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));

            ShiroUser shiroUser = ShiroFactroy.me().createShiroUser(user);
            tokenCache.setUser(token,shiroUser);

            result.put("token", token);
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    @BussinessLog(value = "第三方登录绑定账号", key = "account", dict = CommonDict.class)
    @RequestMapping(value = "/bindSystemUser", method = RequestMethod.POST)
    public Object bindSystemUser(String account, String password, String rePassword) {
        try {
            HttpServletRequest request = HttpUtil.getRequest();
            String token = getToken(request);
            Boolean isSystemUser = JwtUtil.checkIsSystemUser(token);
            if(isSystemUser){
                return Rets.failure("第三方登录的用户才能绑定本系统账号！");
            }

            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return Rets.failure("密码不能为空");
            }
            if (!password.equals(rePassword)) {
                return Rets.failure("新密码前后不一致");
            }
            User user = userService.findByAccount(account);
            if (user == null) {
                return Rets.failure("该用户不存在");
            }
            if (user.getId().intValue() < 1) {
                return Rets.failure("不能绑定系统初始用户");
            }
            String passwdMd5 = MD5.md5(password, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("密码验证失败");
            }

            String otherLoginUserOpenId = JwtUtil.getOtherLoginUserOpenId(token);
            OtherUser otherUser = otherUserService.findByOpenid(otherLoginUserOpenId);
            if (otherUser == null) {
                return Rets.failure("系统错误！");
            }
            if(otherUser.getUserid() != null){
                return Rets.failure("该账号已被绑定，请绑定其他账号！");
            }
            otherUserService.updateByOpenId(user.getId(),otherLoginUserOpenId);

            token = JwtUtil.sign(user,true,null);
            Map<String, String> result = new HashMap<>(1);
            result.put("token", token);
            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));

            ShiroUser shiroUser = ShiroFactroy.me().createShiroUser(user);
            tokenCache.setUser(token,shiroUser);

            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("系统错误！");
    }
}
