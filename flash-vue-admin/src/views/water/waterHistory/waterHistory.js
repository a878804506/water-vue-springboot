import { getList, waterCancel } from '@/api/water/waterHistory'

export default {
  data() {
    return {
      formVisible: false,
      formTitle: '',
      form: {
        reason: '',
        remark: null,
        id: null
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
    waterCancel() {
      if (this.selRow === null) {
        this.$message({
          message: this.$t('common.mustSelectOne'),
          type: 'warning'
        })
        return
      }
      console.log(this.selRow)
      this.formVisible = true
      this.formTitle = '作废' + this.selRow.cname + '的' + this.selRow.remark + '收据'

      this.form.id = this.selRow.cid
      this.form.remark = this.selRow.remark
    },
    waterCancelSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          waterCancel({
            id: this.form.id,
            remark: this.form.remark,
            reason: this.form.reason
          }).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.formVisible = false
            this.fetchData()
          })
        }
      })
    }
  }
}
