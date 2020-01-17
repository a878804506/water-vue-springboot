import {getCustomersWaterCostByMonth} from '@/api/water/waterInfo'

export default {
  data() {
    return {
      month: [{
        value: 1,
        label: '一月'
      }, {
        value: 2,
        label: '二月'
      }, {
        value: 3,
        label: '三月'
      }, {
        value: 4,
        label: '四月'
      }, {
        value: 5,
        label: '五月'
      }, {
        value: 6,
        label: '六月'
      }, {
        value: 7,
        label: '七月'
      }, {
        value: 8,
        label: '八月'
      }, {
        value: 9,
        label: '九月'
      }, {
        value: 10,
        label: '十月'
      }, {
        value: 11,
        label: '十一月'
      }, {
        value: 12,
        label: '十二月'
      }, {
        value: 13,
        label: '1-2月'
      }
      ],
      tableData: null,
      practicalCustomerWaterInfo: null,
      listQuery: {
        month: 1
      },
      listLoading: false
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
  },
  methods: {
    search() {
      if (this.checkInput()) {
        this.listLoading = true
        getCustomersWaterCostByMonth({
          month: this.listQuery.month
        }).then(response => {
          this.$message({
            message: this.$t('common.optionSuccess'),
            type: 'success'
          })
          this.tableData = response.data.tableData
          this.practicalCustomerWaterInfo = response.data.practicalCustomerWaterInfo
          this.listLoading = false
        })
      }
    },
    checkInput() {
      if (this.listQuery.month) {
        return true
      }
      this.$message({
        message: this.$t('common.illegalParameter'),
        type: 'warning'
      })
      return false
    }
  }
}
