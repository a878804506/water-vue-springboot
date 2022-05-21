import {createWaterInfo, downloadExcel, checkMeterCode, getWaterInfo} from '@/api/water/waterInfo'
import {getApiUrl} from '@/utils/utils'
import {getToken} from '@/utils/auth'

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
      listQuery: {
        month: 1,
        cid: undefined,
        meterCode: undefined,
        half: false,
        optType: false,
        capacityCost: 5
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
        // cfgName: [
        //   { required: true, message: this.$t('config.name') + this.$t('common.isRequired'), trigger: 'blur' },
        //   { min: 3, max: 2000, message: this.$t('config.name') + this.$t('config.lengthValidation'), trigger: 'blur' }
        // ]
      }
    }
  },
  created() {
    this.init()
    this.getWaterInfoData()
  },
  methods: {
    init() {
      this.downloadUrl = getApiUrl() + '/water/info/downloadExcel'
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
      this.listQuery.meterCode = ''
      this.waterBill.cid = undefined
      this.waterBill.cname = undefined
      this.waterBill.year = undefined
      this.waterBill.month = undefined
      this.waterBill.firstNumber = undefined
      this.waterBill.lastNumber = undefined
      this.waterBill.waterCount = undefined
      this.waterBill.price = undefined
      this.waterBill.address = undefined
      this.waterBill.meterageCost = undefined
      this.waterBill.charMeterageCost = Array(8)
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
        let month = 1
        if (!this.listQuery.optType) {
          month = this.listQuery.times[1].getMonth() + 1
        } else {
          month = this.listQuery.month
        }
        // 检查录入止码的有效性
        checkMeterCode({
          month: month,
          cid: this.listQuery.cid,
          meterCode: this.listQuery.meterCode
        }).then(response => {
          if (response.data.result) {
            this.createCustomerWaterInfo()
          } else {
            if (response.data.type === 1) {
              this.$message({
                message: response.data.msg,
                type: 'warning'
              })
            } else {
              this.openMsg(response.data.msg)
            }
          }
        })
        this.listLoading = false
        setTimeout(() => {
          this.button.createButton = false
        }, 1000)
      }
    },
    createCustomerWaterInfo() {
      let times = ''
      let month = 1
      if (this.listQuery.optType) {
        times = new Date().getFullYear() + '年' + this.listQuery.month + '月'
        month = this.listQuery.month
      } else {
        times = this.listQuery.times[0].getFullYear() + '年' + (this.listQuery.times[0].getMonth() + 1) + '月-' + this.listQuery.times[1].getFullYear() + '年' + (this.listQuery.times[1].getMonth() + 1) + '月'
        month = this.listQuery.times[1].getMonth() + 1
      }
      createWaterInfo({
        month: month,
        cid: this.listQuery.cid,
        meterCode: this.listQuery.meterCode,
        capacityCost: this.listQuery.capacityCost,
        half: this.listQuery.half,
        optType: this.listQuery.optType,
        times: times
      }).then(response => {
        this.$message({
          message: this.$t('common.createSuccess'),
          type: 'success'
        })
        this.getWaterInfoData()
        this.waterBill = response.data
        if (this.listQuery.month === 13) {
          this.waterBill.month = '1-2'
        }
        this.waterBill.times = times
        this.button.downloadButton = false
      }).catch(() => {
      })
    },
    checkInput() {
      if (this.listQuery.month && this.listQuery.cid && this.listQuery.meterCode) {
        if (this.listQuery.cid > 0 && this.listQuery.meterCode > 0) {
          if (this.reg.test(this.listQuery.cid)) {
            return true
          }
        }
      }
      this.$message({
        message: this.$t('common.mustInputIdAndMeterCode'),
        type: 'warning'
      })
      return false
    },
    downloadExcel() {
      if (this.checkDownload()) {
        if (this.listQuery.month === 13) {
          this.waterBill.month = 13
        }
        window.location.href = this.downloadUrl + '?year=' + this.waterBill.year + '&month=' + this.waterBill.month + '&cid=' + this.waterBill.cid + '&token=' + getToken() + '&times=' + this.waterBill.times
        this.reset()
        this.button.downloadButton = true
      }
    },
    checkDownload() {
      if (this.waterBill.year && this.waterBill.month) {
        return true
      }
      this.$message({
        message: this.$t('common.FirstCreateBill'),
        type: 'warning'
      })
      return false
    },
    openMsg(msg) {
      this.$confirm(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.createCustomerWaterInfo()
      }).catch(() => {
      })
    }
  }
}
