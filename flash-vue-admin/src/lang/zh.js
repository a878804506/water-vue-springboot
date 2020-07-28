export default {
  route: {
    dashboard: '首页',
    system: '系统管理',
    operationMgr: '运维管理',
    cms: 'CMS管理',
    banner: 'banner管理',
    article: '文章管理',
    editArticle: '编辑文章',
    file: '文件管理',
    contacts: '邀约管理',
    channel: '栏目管理',
    menu: '菜单管理',
    dept: '部门管理',
    mgr: '用户管理',
    role: '角色管理',
    task: '任务管理',
    taskLog: '任务日志',
    dict: '字典管理',
    loginLog: '登录日志',
    log: '业务日志',
    cfg: '参数管理',
    druid: '监控管理',
    swagger: '接口文档',
    messageMgr: '消息管理',
    msg: '历史消息',
    msgTpl: '消息模板',
    msgSender: '消息发送器',

    //  水务系统 开始
    waterCustomer: '水务客户',
    customerMgr: '客户管理',

    waterCost: '水费管理',
    customerWaterInfo: '账单管理',
    customerWaterMeter: '起止码管理',
    waterCostByMonth: '全镇月度水费详情',
    waterCostByToDay: '今日录入详情',
    //  水务系统 结束

    //进销存 开始
    commodity: '进销存',
    commodityInfo: '商品管理',
    commodityInventory: '商品库存',
    commodityInventoryStatistics: '出入库统计',
    commoditySalesStatistics: '销售统计',
    //进销存 结束

    //音乐 开始
    music: '一云音乐',
    musicSync: '网络音乐同步',
    musicSyncTask: '音乐同步任务',
    musicNetwork: '网络音乐',
    musicStation: '站内音乐',
    //音乐 结束

    //pdf 开始
    pdf: 'pdf管理',
    pdfManagement: 'pdf在线解析',
    //pdf 结束

    //movie 开始
    movie: '免费视频',
    movieUrl: '视频URL管理',
    moviePlay: '视频播放'
    //movie 结束
  },
  navbar: {
    logOut: '退出登录',
    profile: '个人资料',
    updatePwd: '修改密码',
    dashboard: '首页',
    github: '项目地址',
    screenfull: '全屏',
    theme: '换肤',
    size: '布局大小'
  },
  login: {
    title: '一云智慧云平台',
    errorAccount: '请输入5到32位的数字和字母',
    errorPassword: '密码至少位5位',
    logIn: '登录',
    username: '账号',
    password: '密码',
    otherLogin: '第三方登录',
    otherLoginTips: '请使用一下方式进行登陆'
  },

  button: {
    add: '添加',
    edit: '修改',
    delete: '删除',
    search: '搜索',
    reset: '重置',
    submit: '提交',
    cancel: '取消',
    clear: '清除',
    back: '返回',
    export: '导出',
    //  水务系统 开始
    waterCreateTable: '账单生成',
    waterDownloadExcel: '下载Excel',
    //  水务系统 结束

    // 进销存 开始
    inInventory: '入库',
    outInventory: '出库',
    // 进销存 结束

    //音乐 开始
    musicSync: '同步勾选音乐至本站'
    //音乐 结束
  },
  common: {
    mustSelectOne: '请选中操作项!',
    deleteConfirm: '你确认删除该记录？',
    tooltip: '提示',
    warning: '警告',
    optionSuccess: '操作成功',
    uploadError: '上传文件失败',
    isRequired: '不能为空',
    twoPasswordsAreInconsistent: '两次密码不一致',
    bindMessage: '绑定成功',
    week: {
      mon: '周一',
      tue: '周二',
      wed: '周三',
      thu: '周四',
      fri: '周五',
      sat: '周六',
      sun: '周日'
    },
    //  水务系统 开始
    mustInputIdAndMeterCode: '非法参数:客户id或本月止码!',
    FirstCreateBill: '请先生成账单!',
    createSuccess: '生成成功',
    illegalParameter: '非法参数!',
    toDayNoData: '今日还没给任何客户录入水费!'
    //  水务系统 结束
  },
  dashboard: {
    newUser: '新增用户',
    message: '未处理消息',
    income: '收入',
    onlineUser: '在线用户',
    document: '在线文档',
    date: '日期',
    name: '名称',
    addr: '地址',
    email: '邮件营销',
    ad: '联盟广告',
    vedio: '视频广告',
    direct: '直接访问',
    searchEngine: '搜索引擎',
    userFrom: '用户来源'
  },
  config: {
    name: '参数名',
    value: '参数值',
    descript: '备注',
    add: '添加参数',
    edit: '编辑参数',
    nameInput: '请输入参数名',
    valueInput: '请输入参数值',
    lengthValidation: '长度在 2 到 20 个字符'
  }
}

