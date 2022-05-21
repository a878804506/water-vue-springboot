import { getData } from '@/api/water/waterStatistics'

export default {
  data() {
    return {
      date: null,
      listQuery: {
        year: 2022,
        month: 5
      },
      activeName: 'first',
      waterInfos: [],
      waterInfoTotal: 0,
      waterCustomers: []
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
      this.date = new Date()
      this.listQuery.year = this.date.getFullYear()
      this.listQuery.month = this.date.getMonth() + 1
      this.fetchData()
    },
    changeDatePicker(e) {
      console.log(e)
      this.date = e
      if (e) {
        this.listQuery.year = this.date.getFullYear()
        this.listQuery.month = this.date.getMonth() + 1
      } else {
        this.date = new Date()
        this.listQuery.year = this.date.getFullYear()
        this.listQuery.month = this.date.getMonth() + 1
      }
    },
    tabClick(tab, event) {
      console.log(tab, event)
    },
    fetchData() {
      this.listLoading = true
      getData(this.listQuery).then(response => {
        this.waterInfos = response.data.waterInfo
        this.waterInfoTotal = response.data.total
        this.waterCustomers = response.data.waterCustomer
        this.listLoading = false
      })
    },
    search() {
      this.fetchData()
    },
    reset() {
      this.listQuery.cname = ''
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getData()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
  }
}
