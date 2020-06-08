export default {
  route: {
    dashboard: 'Dashboard',
    system: 'System',
    operationMgr: 'Operation',
    messageMgr: 'Message',
    cms: 'Cms',
    menu: 'Menu',
    dept: 'Department',
    mgr: 'Account',
    role: 'Role',
    task: 'Cron',
    taskLog: 'Cron Log',
    dict: 'Dict',
    loginLog: 'Login Log',
    log: 'Bussiness Log',
    cfg: 'Config',
    banner: 'Banner',
    article: 'Article',
    editArticle: 'Edit Article',
    file: 'File',
    contacts: 'Contacts',
    channel: 'Channel',
    druid: 'Monitor',
    swagger: 'Docs',
    msg: 'History',
    msgTpl: 'Template',
    msgSender: 'Sender',

    //  水务系统 开始
    waterCustomer: 'Water System Customer',
    customerMgr: 'Customer ',

    waterCost: 'Water System Cost',
    customerWaterInfo: 'Customer Water Info',
    customerWaterMeter: 'Customer Water Meter',
    waterCostByMonth: 'Month Water Cost',
    waterCostByToDay: 'ToDay Water Cost',
    //  水务系统 结束

    //进销存 开始
    commodity: 'Inventory managerment',
    commodityInfo: 'Commondity managerment',
    commodityInventory: 'Commodity Inventory',
    commodityInventoryStatistics: 'Inventory Statistics',
    commoditySalesStatistics: 'Sales Statistics',
    //进销存 结束

    //音乐 开始
    music: 'OneCloud Music',
    musicSync: 'NetWork Music Sync'
    //音乐 结束
  },
  navbar: {
    logOut: 'Log Out',
    dashboard: 'Dashboard',
    profile: 'Profile',
    updatePwd: 'Modify Password',
    github: 'Github',
    screenfull: 'Screenfull',
    theme: 'Theme',
    size: 'Global Size'
  },
  login: {
    title: 'One Cloud',
    errorAccount: 'Please enter the correct user name',
    errorPassword: 'The password can not be less than 5 digits',
    logIn: 'Log in',
    username: 'Username',
    password: 'Password',
    otherLogin: 'Other Login',
    otherLoginTips: 'Please Choose a Way To Login'
  },
  button: {
    add: 'Add',
    edit: 'Edit',
    delete: 'Delete',
    search: 'Search',
    reset: 'Reset',
    submit: 'Submit',
    cancel: 'Cancel',
    clear: 'Clear',
    back: 'Back',
    export: 'Export',
    //  水务系统 开始
    waterCreateTable: 'Create Bill',
    waterDownloadExcel: '下载Excel',
    //  水务系统 结束

    // 进销存 开始
    inInventory: 'In Inventory',
    outInventory: 'Out Inventory',
    // 进销存 结束

    //音乐 开始
    musicSync: 'Sync Music'
    //音乐 结束
  },
  common: {
    mustSelectOne: 'Please select the record to operate!',
    deleteConfirm: 'Are you sure delete the record?',
    tooltip: 'Tooltip',
    warning: 'Warning',
    optionSuccess: 'success',
    uploadError: 'Upload Error',
    isRequired: ' is required',
    twoPasswordsAreInconsistent: 'Two Passwords Are Inconsistent',
    bindMessage: 'Bind Success',
    week: {
      mon: 'Mon',
      tue: 'Tue',
      wed: 'Wed',
      thu: 'Thu',
      fri: 'Fri',
      sat: 'Sat',
      sun: 'Sun'
    },
    //  水务系统 开始
    mustInputIdAndMeterCode: 'illegal parameter:the customer ID or meter code !',
    FirstCreateBill: 'Please create customer bill first!',
    createSuccess: 'Create Success',
    illegalParameter: 'Illegal parameter!',
    toDayNoData: 'Not create Any customer bill ToDay !'
    //  水务系统 结束
  },

  dashboard: {
    newUser: 'New Users',
    message: 'Message',
    income: 'Income',
    onlineUser: 'Online User',
    document: 'Document',
    date: 'Date',
    name: 'Name',
    addr: 'Address',
    // '邮件营销','联盟广告','视频广告','直接访问','搜索引擎'
    email: 'Email',
    ad: 'Ad',
    vedio: 'Vedio',
    direct: 'Direct',
    searchEngine: 'Search engine',
    userFrom: 'User from'
  },
  config: {
    name: 'Config Name',
    value: 'Config Value',
    descript: 'Descript',
    add: 'Add Config',
    edit: 'Edit Config',
    nameInput: 'Input config name',
    valueInput: 'Input config value',
    lengthValidation: ' must be between 2 and 20 characters'
  }
}
