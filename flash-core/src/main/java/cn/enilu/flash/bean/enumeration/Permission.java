package cn.enilu.flash.bean.enumeration;

/**
 * 权限编码列表<br>
 * 权限编码需要和菜单中的菜单编码一致
 * @author ：enilu
 * @date ：Created in 2019/7/31 11:05
 */
public interface Permission {

    //系统管理
    String CFG = "cfg";
    String CFG_EDIT = "cfgEdit";
    String CFG_DEL = "cfgDelete";
    String DICT = "dict";
    String DICT_EDIT = "dictEdit";
    String LOG = "log";
    String LOG_CLEAR = "logClear";
    String LOGIN_LOG = "loginLog";
    String LOGIN_LOG_CLEAR = "loginLogClear";
    String ROLE = "role";
    String ROLE_EDIT = "roleEdit";
    String ROLE_DEL = "roleDelete";
    String TASK = "task";
    String TASK_EDIT = "taskEdit";
    String TASK_DEL = "taskDelete";
    String MENU = "menu";
    String MENU_EDIT = "menuEdit";
    String MENU_DEL = "menuDelete";
    String USER = "mgr";
    String USER_EDIT = "mgrEdit";
    String USER_DEL = "mgrDelete";
    String DEPT = "dept";
    String DEPT_EDIT = "deptEdit";
    String DEPT_DEL = "deptDelete";

    //消息管理
    String MSG = "msg";
    String MSG_CLEAR = "msgClear";
    String MSG_SENDER = "msgSender";
    String MSG_SENDER_EDIT = "msgSenderEdit";
    String MSG_SENDER_DEL = "msgSenderDelete";
    String MSG_TPL = "msgTpl";
    String MSG_TPL_EDIT = "msgTplEdit";
    String MSG_TPL_DEL = "msgTplDelete";

    //CMS管理
    String ARTICLE = "article";
    String ARTICLE_EDIT = "editArticle";
    String ARTICLE_DEL = "deleteArticle";
    String BANNER = "banner";
    String BANNER_EDIT = "bannerEdit";
    String BANNER_DEL = "bannerDelete";
    String CHANNEL = "channel";
    String CHANNEL_EDIT = "channelEdit";
    String CHANNEL_DEL = "channelDelete";
    String CONTACTS = "contacts";
    String FILE = "file";
    String FILE_UPLOAD = "fileUpload";

    // 水务系统  开始
    // 客户信息
    String CUSTOMER_MGR = "customerMgr";
    String CUSTOMER_INSERT_UPDATE = "customerInsertUpdate";
    //客户起止码
    String CUSTOMER_WATER_METER = "customerWaterMeter";
    String UPDATE_WATER_METER = "updateWaterMeter";
    //客户账单
    String CREATE_WATER_INFO = "createWaterInfo";
    String DOWNLOAD_WATER_INFO = "downloadWaterInfo";
    String CUSTOMER_WATER_INFO = "getWaterInfo";
    //全镇月度总水费详情
    String CUSTOMERS_WATER_COST = "waterCostByMonth";
    // 水务系统  结束
}
