import { getList } from '@/api/water/waterHistory'

export default {
  data() {
    return {
      formVisible: false,
      formTitle: '',
      form: {
        reason: '',
        id: ''
      },
      listQuery: {
        page: 1,
        limit: 10,
        id: undefined
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: null
    }
  },
  filters: {

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
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getList(this.listQuery).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.listQuery.page = 1
      this.fetchData()
    },
    reset() {
      this.listQuery.cname = ''
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
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
    cancel() {
      if (this.selRow === null) {
        this.$message({
          message: this.$t('common.mustSelectOne'),
          type: 'warning'
        })
        return
      }
      console.log(this.selRow)
      this.$message({
        message: this.$t('该功能暂未实现，耐心等待！'),
        type: 'success'
      })

    }
  }
}
