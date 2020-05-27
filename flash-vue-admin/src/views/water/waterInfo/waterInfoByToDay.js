import {getToDayTabs, getToDayData} from '@/api/water/waterInfo'

export default {
  data() {
    return {
      tabs: null,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 50
      },
      totalDatas: [],
      customerDatas: []
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  computed: {
    // 表单验证
    rules() {
      return {
        // cfgName: [
        //   { required: true, message: this.$t('config.name') + this.$t('common.isRequired'), trigger: 'blur' },
        //   { min: 3, max: 2000, message: this.$t('config.name') + this.$t('config.lengthValidation'), trigger: 'blur' }
        // ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.listLoading = true
      getToDayTabs().then(response => {
        if (response.data.length === 0) {
          this.$message({
            message: this.$t('common.toDayNoData'),
            type: 'warning'
          })
          this.listLoading = false
          return
        }
        this.tabs = response.data
        // 进入页面查询第一页数据
        this.getToDayData()
      })
    },
    getToDayData() {
      getToDayData(this.listQuery).then(response => {
        this.totalDatas = response.data.totalDatas
        this.customerDatas = response.data.customerDatas
        this.listLoading = false
      })
    },
    clickTab(tab, event) {
      this.listQuery.page = tab.index - 0 + 1
      this.listLoading = true
      this.getToDayData()
    }
  }
}
