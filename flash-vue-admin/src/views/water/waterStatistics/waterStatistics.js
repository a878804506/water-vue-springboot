import { getData } from '@/api/water/waterStatistics'
import { getApiUrl } from '@/utils/utils'
import { getToken } from '@/utils/auth'
import { parseTime } from '../../../utils'

export default {
  data() {
    return {
      serchDate: [null, null],
      listLoading: false,
      activeName: 'first',
      waterInfos: [],
      waterInfoTotal: 0,
      waterCustomers: [],
      downloadBtnDisabeld: true,
      downloadUrl: null
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

    //表单验证
    rules() {
      return {

      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.downloadUrl = getApiUrl() + '/water/waterhistory/monthStatisticsExport'
      let startDate = new Date()
      let endDate = new Date()
      if (endDate.getMonth() === 0) {
        startDate.setFullYear(startDate.getFullYear() - 1, 11, 21)
      } else {
        startDate.setMonth(startDate.getMonth() - 1, 21)
      }
      endDate.setDate(20)

      startDate.setHours(0)
      startDate.setMinutes(0)
      startDate.setSeconds(0)

      endDate.setHours(23)
      endDate.setMinutes(59)
      endDate.setSeconds(59)
      this.serchDate[0] = startDate
      this.serchDate[1] = endDate
    },
    changeDatePicker(e) {
      e[1].setHours(23)
      e[1].setMinutes(59)
      e[1].setSeconds(59)
      if (e) {
        this.serchDate = e
      }
      console.log(this.serchDate[0])
      console.log(this.serchDate[1])
    },
    tabClick(tab, event) {
      console.log(tab, event)
    },
    fetchData() {
      this.listLoading = true
      let startDate = parseTime(this.serchDate[0])
      let endDate = parseTime(this.serchDate[1])
      console.log(startDate)
      console.log(endDate)
      getData({startDate, endDate}).then(response => {
        this.waterInfos = response.data.waterInfo
        this.waterInfoTotal = response.data.total
        this.waterCustomers = response.data.waterCustomer
        this.listLoading = false
        if (response.data.waterInfo && response.data.waterInfo.length != 0){
          this.downloadBtnDisabeld = false
        } else {
          this.downloadBtnDisabeld = true
        }
      })
    },
    search() {
      this.fetchData()
    },
    downloadExcel() {
      let startDate = parseTime(this.serchDate[0])
      let endDate = parseTime(this.serchDate[1])
      console.log(startDate)
      console.log(endDate)
      window.location.href = this.downloadUrl + '?startDate=' + startDate + '&endDate=' + endDate +  '&token=' + getToken()
      this.downloadBtnDisabeld = true
    },
  }
}
