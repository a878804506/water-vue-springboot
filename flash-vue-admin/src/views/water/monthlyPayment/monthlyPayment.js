import { getWaterInfo } from '@/api/water/waterInfo'
import { createWaterMonthlyPayment } from '@/api/water/waterMonthlyPayment'
import { getApiUrl } from '@/utils/utils'
import { getToken } from '@/utils/auth'

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
      }
      ],
      listQuery: {
        month: 1,
        cid: undefined,
        half: false,
        price: 20
      },
      waterBill: {
        cid: undefined,
        cname: undefined,
        year: undefined,
        month: undefined,
        firstNumber: undefined,
        lastNumber: undefined,
        waterCount: undefined,
        price: undefined,
        address: undefined,
        meterageCost: undefined,
        charMeterageCost: Array(8),
        capacityCost: undefined,
        charCapacityCost: Array(8),
        waterCost: undefined,
        charWaterCost: Array(8),
        capitalization: undefined,
        excelFileName: undefined,
        times: undefined
      },
      button: {
        createButton: false,
        downloadButton: true
      },
      listLoading: false,
      reg: new RegExp("^[0-9]*$"),
      downloadUrl: '',
      toDayCount: 0,
      toMonthCount: 0
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

      }
    }
  },
  created() {
    this.init()
    this.getWaterInfoData()
  },
  methods: {
    init() {
      this.downloadUrl = getApiUrl() + '/water/monthlyPayment/downloadExcel'
    },
    getWaterInfoData() {
      getWaterInfo({
        month: this.listQuery.month
      }).then(response => {
        this.toDayCount = response.data.toDay
        this.toMonthCount = response.data.toMonth
      })
    },
    changeDatePicker(e) {
      if (e) {
        let months = (e[1].getFullYear() - e[0].getFullYear()) * 12 + (e[1].getMonth() - e[0].getMonth()) + 1
        console.log(months)
        this.listQuery.capacityCost = months * 5
      } else {
        this.listQuery.capacityCost = 5
      }
    },
    reset() {
      this.listQuery.cid = ''
      this.waterBill.cid = undefined
      this.waterBill.cname = undefined
      this.waterBill.month = undefined
      this.waterBill.waterCount = undefined
      this.waterBill.price = undefined
      this.waterBill.address = undefined
      this.waterBill.capacityCost = undefined
      this.waterBill.charCapacityCost = Array(8)
      this.waterBill.waterCost = undefined
      this.waterBill.charWaterCost = Array(8)
      this.waterBill.capitalization = undefined
      this.waterBill.excelFileName = undefined
      this.waterBill.times = ''
    },
    create() {
      if (this.checkInput()) {
        this.listLoading = true
        this.button.createButton = true

        this.createCustomerWaterInfo()
        this.listLoading = false
        setTimeout(() => {
          this.button.createButton = false
        }, 1000)
      }
    },
    createCustomerWaterInfo() {
      let months = this.listQuery.times[1].getMonth() - this.listQuery.times[0].getMonth() + 1
      let times = this.listQuery.times[0].getFullYear() + '年' + (this.listQuery.times[0].getMonth() + 1) + '月-' + this.listQuery.times[1].getFullYear() + '年' + (this.listQuery.times[1].getMonth() + 1) + '月'

      createWaterMonthlyPayment({
        months: months,
        cid: this.listQuery.cid,
        price: this.listQuery.price,
        half: this.listQuery.half,
        times: times
      }).then(response => {
        this.$message({
          message: this.$t('common.createSuccess'),
          type: 'success'
        })
        this.getWaterInfoData()
        this.waterBill = response.data
        this.waterBill.times = times
        this.button.downloadButton = false
      }).catch(() => {
      })
    },
    checkInput() {
      if (this.listQuery.times && this.listQuery.cid && this.listQuery.price) {
        if (this.listQuery.cid > 0 && this.listQuery.price > 0) {
          if (this.reg.test(this.listQuery.cid)) {
            return true
          }
        }
      }
      this.$message({
        message: this.$t('common.mustInputIdAndPrice'),
        type: 'warning'
      })
      return false
    },
    downloadExcel() {
      if (this.checkDownload()) {
        window.location.href = this.downloadUrl + '?cid=' + this.waterBill.cid + '&token=' + getToken() + '&times=' + this.waterBill.times
        this.reset()
        this.button.downloadButton = true
      }
    },
    checkDownload() {
      if (this.listQuery.times && this.waterBill.cid && this.waterBill.price) {
        return true
      }
      this.$message({
        message: this.$t('common.FirstCreateBill'),
        type: 'warning'
      })
      return false
    }
  }
}
